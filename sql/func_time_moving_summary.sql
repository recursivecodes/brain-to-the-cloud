create or replace function timeMovingSummary(withBrain in number) return varchar2 sql_macro is
begin
    return q'{
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
            cast(case when minutesPlayed > 0 then kills / minutesPlayed else 0 end as number(18,2)) as killsPerMinute,
            case when timeMovingSummary.withBrain = 1 then cast(coalesce(avgAttention, 0) as number(18,2)) else null end as avgAttention,
            case when timeMovingSummary.withBrain = 1 then cast(coalesce(avgMeditation, 0) as number(18,2)) else null end as avgMeditation
        from ranges r
            left outer join timeMovingRange(timeMovingSummary.withBrain) d
                on r.timeRange = d.timeRange
        order by r.timeRange }';
end timeMovingSummary;