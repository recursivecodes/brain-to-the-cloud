drop view vw_summary_by_attention_range;

create view vw_summary_by_attention_range
as
with ranges as (
    --select '0' as attentionRange from dual union all
    select '1-10' as attentionRange from dual union all
    select '11-20' as attentionRange from dual union all
    select '21-30' as attentionRange from dual union all
    select '31-40' as attentionRange from dual union all
    select '41-50' as attentionRange from dual union all
    select '51-60' as attentionRange from dual union all
    select '61-70' as attentionRange from dual union all
    select '71-80' as attentionRange from dual union all
    select '81-90' as attentionRange from dual union all
    select '91-100' as attentionRange from dual
),
data as (
    select 
        case 
            --when when avgAttention = 0 then '0'
            when avgAttention >= 1 and avgAttention <= 10 then '1-10'
            when avgAttention > 10 and avgAttention <= 20 then '11-20'
            when avgAttention > 20 and avgAttention <= 30 then '21-30'
            when avgAttention > 30 and avgAttention <= 40 then '31-40'
            when avgAttention > 40 and avgAttention <= 50 then '41-50'
            when avgAttention > 50 and avgAttention <= 60 then '51-60'
            when avgAttention > 60 and avgAttention <= 70 then '61-70'
            when avgAttention > 70 and avgAttention <= 80 then '71-80'
            when avgAttention > 80 and avgAttention <= 90 then '81-90'
            when avgAttention > 90 and avgAttention <= 100 then '91-100' 
        else ''
        end as attentionRange,
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
    where brainRecords > 0
    group by 
        case 
            --when when avgAttention = 0 then '0'
            when avgAttention >= 1 and avgAttention <= 10 then '1-10'
            when avgAttention > 10 and avgAttention <= 20 then '11-20'
            when avgAttention > 20 and avgAttention <= 30 then '21-30'
            when avgAttention > 30 and avgAttention <= 40 then '31-40'
            when avgAttention > 40 and avgAttention <= 50 then '41-50'
            when avgAttention > 50 and avgAttention <= 60 then '51-60'
            when avgAttention > 60 and avgAttention <= 70 then '61-70'
            when avgAttention > 70 and avgAttention <= 80 then '71-80'
            when avgAttention > 80 and avgAttention <= 90 then '81-90'
            when avgAttention > 90 and avgAttention <= 100 then '91-100' 
        else ''
    end
)

select 
    r.attentionRange as "RANGE", 
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
    cast(case when minutesPlayed > 0 then kills / minutesPlayed else 0 end as number(18,2)) as killsPerMinute,
    cast(coalesce(avgAttention, 0) as number(18,2)) as avgAttention,
    cast(coalesce(avgMeditation, 0) as number(18,2)) as avgMeditation
from ranges r
    left outer join data d
        on r.attentionRange = d.attentionRange
order by r.attentionRange;        