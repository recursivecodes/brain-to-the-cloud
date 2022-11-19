create or replace function rangeSummary(factor in varchar2, game in varchar2) return varchar2 sql_macro is
begin
    return q'{
    with ranges as (
        select '<.50' as "range", 'ratio' as factor from dual union all
        select '.50-.59' as "range", 'ratio' as factor from dual union all
        select '.60-.69' as "range", 'ratio' as factor from dual union all
        select '.70-.79' as "range", 'ratio' as factor from dual union all
        select '.80-.89' as "range", 'ratio' as factor from dual union all
        select '.90-.99' as "range", 'ratio' as factor from dual union all
        select '1.0-1.1' as "range", 'ratio' as factor from dual union all
        select '1.1-1.2' as "range", 'ratio' as factor from dual union all
        select '>1.2' as "range", 'ratio' as factor from dual union all
        select '1-10' as "range", 'attentionMeditation' as factor from dual union all
        select '11-20' as "range", 'attentionMeditation' as factor from dual union all
        select '21-30' as "range", 'attentionMeditation' as factor from dual union all
        select '31-40' as "range", 'attentionMeditation' as factor from dual union all
        select '41-50' as "range", 'attentionMeditation' as factor from dual union all
        select '51-60' as "range", 'attentionMeditation' as factor from dual union all
        select '61-70' as "range", 'attentionMeditation' as factor from dual union all
        select '71-80' as "range", 'attentionMeditation' as factor from dual union all
        select '81-90' as "range", 'attentionMeditation' as factor from dual union all
        select '91-100' as "range", 'attentionMeditation' as factor from dual
    )
    select
        r."range" as "RANGE",
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
        cast(coalesce(avgMeditation, 0) as number(18,2)) as avgMeditation,
        cast(coalesce(avgAmRatio, 0) as number(18,2)) as avgAmRatio
    from ranges r
        left outer join summaryByRange(rangeSummary.factor, rangeSummary.game) d
            on r."range" = d."range"
    where ((rangeSummary.factor = 'attention' OR rangeSummary.factor = 'meditation') AND r.factor = 'attentionMeditation')
    or (rangeSummary.factor = 'ratio' AND r.factor = 'ratio')
    order by
    case
        when r."range" = '<.50' then 1
        when r."range" = '.50-.59' then 2
        when r."range" = '.60-.69' then 3
        when r."range" = '.70-.79' then 4
        when r."range" = '.80-.89' then 5
        when r."range" = '.90-.99' then 6
        when r."range" = '1.0-1.1' then 7
        when r."range" = '1.1-1.2' then 8
        when r."range" = '>1.2' then 9
        else 10
    end, r."range"
    }';
end rangeSummary;