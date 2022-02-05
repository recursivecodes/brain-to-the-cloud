drop view vw_summary_by_meditation_range;

create view vw_summary_by_meditation_range
as
with ranges as (
    --select '0' as meditationRange from dual union all
    select '1-10' as meditationRange from dual union all
    select '11-20' as meditationRange from dual union all
    select '21-30' as meditationRange from dual union all
    select '31-40' as meditationRange from dual union all
    select '41-50' as meditationRange from dual union all
    select '51-60' as meditationRange from dual union all
    select '61-70' as meditationRange from dual union all
    select '71-80' as meditationRange from dual union all
    select '81-90' as meditationRange from dual union all
    select '91-100' as meditationRange from dual
),
data as (
    select 
        case 
            --when when avgMeditation = 0 then '0'
            when avgMeditation >= 1 and avgMeditation <= 10 then '1-10'
            when avgMeditation > 10 and avgMeditation <= 20 then '11-20'
            when avgMeditation > 20 and avgMeditation <= 30 then '21-30'
            when avgMeditation > 30 and avgMeditation <= 40 then '31-40'
            when avgMeditation > 40 and avgMeditation <= 50 then '41-50'
            when avgMeditation > 50 and avgMeditation <= 60 then '51-60'
            when avgMeditation > 60 and avgMeditation <= 70 then '61-70'
            when avgMeditation > 70 and avgMeditation <= 80 then '71-80'
            when avgMeditation > 80 and avgMeditation <= 90 then '81-90'
            when avgMeditation > 90 and avgMeditation <= 100 then '91-100' 
        else ''
        end as meditationRange,
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
        avg(avgMeditation) as avgMeditation
    from mv_game_details_with_brain
    group by 
        case 
            --when when avgMeditation = 0 then '0'
            when avgMeditation >= 1 and avgMeditation <= 10 then '1-10'
            when avgMeditation > 10 and avgMeditation <= 20 then '11-20'
            when avgMeditation > 20 and avgMeditation <= 30 then '21-30'
            when avgMeditation > 30 and avgMeditation <= 40 then '31-40'
            when avgMeditation > 40 and avgMeditation <= 50 then '41-50'
            when avgMeditation > 50 and avgMeditation <= 60 then '51-60'
            when avgMeditation > 60 and avgMeditation <= 70 then '61-70'
            when avgMeditation > 70 and avgMeditation <= 80 then '71-80'
            when avgMeditation > 80 and avgMeditation <= 90 then '81-90'
            when avgMeditation > 90 and avgMeditation <= 100 then '91-100' 
        else ''
    end
)

select 
    r.meditationRange as "RANGE", 
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
        on r.meditationRange = d.meditationRange
order by r.meditationRange;        