drop view vw_summary_by_am_ratio_range;

create view vw_summary_by_am_ratio_range
as
with ranges as (
    select '<.5' as amRange from dual union all
    select '.5-.6' as amRange from dual union all
    select '.6-.7' as amRange from dual union all
    select '.7-.8' as amRange from dual union all
    select '.8-.9' as amRange from dual union all
    select '.9-1' as amRange from dual union all
    select '1-1.1' as amRange from dual union all
    select '1.1-1.2' as amRange from dual union all
    select '>1.2' as amRange from dual
),
data as (
    select 
        case 
            when amRatio < .5 then '<.5'
            when amRatio >= .5 and amRatio < .6 then '.5-.6'
            when amRatio >= .6 and amRatio < .7 then '.6-.7'            
            when amRatio >= .7 and amRatio < .8 then '.7-.8'
            when amRatio >= .8 and amRatio < .9 then '.8-.9'
            when amRatio >= .9 and amRatio < 1 then '.9-1'
            when amRatio >= 1 and amRatio < 1.1 then '1-1.1'
            when amRatio >= 1.1 and amRatio < 1.2 then '1.1-1.2'
            when amRatio >= 1.2 then '>1.2'
            else 'unknown'
        end as amRange,
        sum(kills) as kills,
        sum(assists) as assists,
        sum(deaths) as deaths,
        sum(score) as score,
        avg(score) as avgScore,
        avg(accuracy) as avgAccuracy,
        sum(timePlayed) as timePlayed,
        sum(timePlayed/60) as minutesPlayed,
        sum( case when "result" = 'loss' then 1 else 0 end ) as totalLosses,
        sum( case when "result" = 'win' then 1 else 0 end ) as totalWins,
        avg(avgAttention) as avgAttention,
        avg(avgMeditation) as avgMeditation,
        avg(amRatio) as avgAmRatio
    from mv_game_details_with_brain
    group by 
        case 
            when amRatio < .5 then '<.5'
            when amRatio >= .5 and amRatio < .6 then '.5-.6'
            when amRatio >= .6 and amRatio < .7 then '.6-.7'            
            when amRatio >= .7 and amRatio < .8 then '.7-.8'
            when amRatio >= .8 and amRatio < .9 then '.8-.9'
            when amRatio >= .9 and amRatio < 1 then '.9-1'
            when amRatio >= 1 and amRatio < 1.1 then '1-1.1'
            when amRatio >= 1.1 and amRatio < 1.2 then '1.1-1.2'
            when amRatio >= 1.2 then '>1.2'
            else 'unknown'
        end   
)

select 
    r.amRange as "RANGE", 
    coalesce(totalWins, 0) as totalWins,
    coalesce(totalLosses, 0) as totalLosses,
    cast(
        case 
            when coalesce(totalLosses, 0) = 0 and coalesce(totalWins, 0) = 0 then 0 
            when totalLosses > 0 then totalWins / totalLosses 
        else 1 end
    as number(18,2)) as wlRatio,
    coalesce(kills, 0) as kills,
    coalesce(deaths, 0) as deaths,
    cast(case when deaths > 0 then kills/deaths else 0 end as number(18,2)) as kdRatio,
    cast(case when deaths > 0 then (kills+assists)/deaths else 0 end as number(18,2)) as edRatio,
    coalesce(score, 0) as score,
    cast(coalesce(avgScore, 0) as number(18,2)) as avgScore,
    cast(coalesce(avgAccuracy, 0) as number(18,2)) as averageAccuracy,
    coalesce(timePlayed, 0) as timePlayed,
    cast(case when minutesPlayed > 0 then score / minutesPlayed else 0 end as number(18,2)) as scorePerMinute,
    cast(coalesce(avgAttention, 0) as number(18,2)) as avgAttention,
    cast(coalesce(avgMeditation, 0) as number(18,2)) as avgMeditation
from ranges r
    left outer join data d
        on r.amRange = d.amRange
order by 
    case 
        when r.amRange = '<.5' then 1
        when r.amRange = '.5-.6' then 2
        when r.amRange = '.6-.7' then 3
        when r.amRange = '.7-.8' then 4
        when r.amRange = '.8-.9' then 5
        when r.amRange = '.9-1' then 6
        when r.amRange = '1-1.1' then 7
        when r.amRange = '1.1-1.2' then 8
        when r.amRange = '>1.2' then 9
        else 10
    end     