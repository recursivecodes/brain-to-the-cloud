drop view vw_summary_by_time_moving;

create view vw_summary_by_time_moving
as
with ranges as (
    --select '0' as timeRange from dual union all
    select '1-10' as timeRange from dual union all
    select '11-20' as timeRange from dual union all
    select '21-30' as timeRange from dual union all
    select '31-40' as timeRange from dual union all
    select '41-50' as timeRange from dual union all
    select '51-60' as timeRange from dual union all
    select '61-70' as timeRange from dual union all
    select '71-80' as timeRange from dual union all
    select '81-90' as timeRange from dual union all
    select '91-100' as timeRange from dual
),
data as (
    select
    case
        --when percentTimeMoving = 0 then '0'
        when percentTimeMoving >= 1 and percentTimeMoving <= 10 then '1-10'
        when percentTimeMoving > 10 and percentTimeMoving <= 20 then '11-20'
        when percentTimeMoving > 20 and percentTimeMoving <= 30 then '21-30'
        when percentTimeMoving > 30 and percentTimeMoving <= 40 then '31-40'
        when percentTimeMoving > 40 and percentTimeMoving <= 50 then '41-50'
        when percentTimeMoving > 50 and percentTimeMoving <= 60 then '51-60'
        when percentTimeMoving > 60 and percentTimeMoving <= 70 then '61-70'
        when percentTimeMoving > 70 and percentTimeMoving <= 80 then '71-80'
        when percentTimeMoving > 80 and percentTimeMoving <= 90 then '81-90'
        when percentTimeMoving > 90 and percentTimeMoving <= 100 then '91-100'
        else ''
    end as timeRange,
    sum(kills) as kills,
    sum(assists) as assists,
    sum(deaths) as deaths,
    sum(score) as score,
    avg(score) as avgScore,
    avg(accuracy) as avgAccuracy,
    sum(timePlayed) as timePlayed,
    sum(timePlayed/60) as minutesPlayed,
    sum( case when "result" = 'loss' then 1 else 0 end ) as totalLosses,
    sum( case when "result" = 'win' then 1 else 0 end ) as totalWins
    from mv_game_details
    group by
        case
            --when percentTimeMoving = 0 then '0'
            when percentTimeMoving >= 1 and percentTimeMoving <= 10 then '1-10'
            when percentTimeMoving > 10 and percentTimeMoving <= 20 then '11-20'
            when percentTimeMoving > 20 and percentTimeMoving <= 30 then '21-30'
            when percentTimeMoving > 30 and percentTimeMoving <= 40 then '31-40'
            when percentTimeMoving > 40 and percentTimeMoving <= 50 then '41-50'
            when percentTimeMoving > 50 and percentTimeMoving <= 60 then '51-60'
            when percentTimeMoving > 60 and percentTimeMoving <= 70 then '61-70'
            when percentTimeMoving > 70 and percentTimeMoving <= 80 then '71-80'
            when percentTimeMoving > 80 and percentTimeMoving <= 90 then '81-90'
            when percentTimeMoving > 90 and percentTimeMoving <= 100 then '91-100'
            else ''
        end
)

select 
    r.timeRange as "RANGE",
    coalesce(totalWins, 0) as totalWins,
    coalesce(totalLosses, 0) as totalLosses,
    cast( 
        case 
            when coalesce(totalLosses, 0) = 0 and coalesce(totalWins, 0) = 0 then 0 
            when totalLosses > 0 then totalWins / totalLosses 
        else 1 end
        as number(18,2)
    ) as wlRatio,
    coalesce(kills, 0) as kills,
    coalesce(deaths, 0) as deaths,
    cast(case when deaths > 0 then kills/deaths else 0 end as number(18,2)) as kdRatio,
    cast(case when deaths > 0 then (kills+assists)/deaths else 0 end as number(18,2)) as edRatio,
    coalesce(score, 0) as score,
    cast(coalesce(avgScore, 0) as number(18,2)) as avgScore,
    cast(coalesce(avgAccuracy, 0) as number(18,2)) as averageAccuracy,
    coalesce(timePlayed, 0) as timePlayed,
    cast(case when minutesPlayed > 0 then score / minutesPlayed else 0 end as number(18,2)) as scorePerMinute,
    cast(case when minutesPlayed > 0 then kills / minutesPlayed else 0 end as number(18,2)) as killsPerMinute

from ranges r
    left outer join data d
        on r.timeRange = d.timeRange
order by r.timeRange;        