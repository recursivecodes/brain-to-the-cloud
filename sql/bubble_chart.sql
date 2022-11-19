with ranges as (
    select '1-10' as attentionRange, '1-10' as meditationRange from dual union all
    select '11-20' as attentionRange, '11-20' as meditationRange from dual union all
    select '21-30' as attentionRange, '21-30' as meditationRange from dual union all
    select '31-40' as attentionRange, '31-40' as meditationRange from dual union all
    select '41-50' as attentionRange, '41-50' as meditationRange from dual union all
    select '51-60' as attentionRange, '51-60' as meditationRange from dual union all
    select '61-70' as attentionRange, '61-70' as meditationRange from dual union all
    select '71-80' as attentionRange, '71-80' as meditationRange from dual union all
    select '81-90' as attentionRange, '81-90' as meditationRange from dual union all
    select '91-100' as attentionRange, '91-100' as meditationRange from dual
)
select
    r.meditationRange,
    r.attentionRange,
    coalesce(d1.totalWins, 0)+coalesce(d2.totalWins, 0) as totalWins,
    coalesce(d1.totalLosses, 0)+coalesce(d2.totalLosses, 0) as totalLosses,
    cast(
            case
                when coalesce(d1.totalLosses, 0)+coalesce(d2.totalLosses, 0) = 0 and coalesce(d1.totalLosses, 0)+coalesce(d2.totalLosses, 0) = 0 then 0
                when coalesce(d1.totalLosses, 0)+coalesce(d2.totalLosses, 0) > 0 then (coalesce(d1.totalWins, 0)+coalesce(d2.totalWins, 0)) / (coalesce(d1.totalLosses, 0)+coalesce(d2.totalLosses, 0))
                else 1 end
        as number(18,2)) as wlRatio,
    coalesce(d1.kills, 0)+coalesce(d2.kills, 0) as kills,
    coalesce(d1.deaths, 0)+coalesce(d2.deaths, 0) as deaths,
    cast(case when coalesce(d1.deaths, 0)+coalesce(d2.deaths, 0) > 0 then (coalesce(d1.kills, 0)+coalesce(d2.kills, 0))/(coalesce(d1.deaths, 0)+coalesce(d2.deaths, 0)) else 0 end as number(18,2)) as kdRatio,
    cast(case when coalesce(d1.deaths, 0)+coalesce(d2.deaths, 0) > 0 then (coalesce(d1.kills, 0)+coalesce(d2.kills, 0)+coalesce(d1.assists, 0)+coalesce(d2.assists, 0))/(coalesce(d1.deaths, 0)+coalesce(d2.deaths, 0)) else 0 end as number(18,2)) as edRatio,
    coalesce(d1.score, 0)+coalesce(d2.score, 0) as score,
    cast(coalesce((coalesce(d1.avgScore, 0)+coalesce(d2.avgScore, 0))/2, 0) as number(18,2)) as avgScore,
    cast(coalesce((coalesce(d1.avgAccuracy, 0)+coalesce(d2.avgAccuracy, 0))/2, 0) as number(18,2)) as averageAccuracy,
    coalesce(d1.timePlayed, 0)+coalesce(d2.timePlayed, 0) as timePlayed,
    cast(case when coalesce(d1.minutesPlayed, 0)+coalesce(d2.minutesPlayed, 0) > 0 then (coalesce(d1.score, 0)+coalesce(d2.score, 0)) / (coalesce(d1.minutesPlayed, 0)+coalesce(d2.minutesPlayed, 0)) else 0 end as number(18,2)) as scorePerMinute,
    cast(case when coalesce(d1.minutesPlayed, 0)+coalesce(d2.minutesPlayed, 0) > 0 then (coalesce(d1.kills, 0)+coalesce(d2.kills, 0)) / (coalesce(d1.minutesPlayed, 0)+coalesce(d2.minutesPlayed, 0)) else 0 end as number(18,2)) as killsPerMinute
from ranges r
         left outer join summaryByRange('meditation') d1
                         on r.meditationRange = d1."range"
         left outer join summaryByRange('attention') d2
                         on r.attentionRange = d2."range"
order by r.attentionRange, r.meditationRange;