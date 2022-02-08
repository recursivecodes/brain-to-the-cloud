create or replace function summaryByRange(factor in varchar2) return varchar2 sql_macro is
begin
    return q'{
        select
            case
                --when when avgMeditation = 0 then '0'
                when summaryByRange.factor = 'meditation' and avgMeditation >= 1 and avgMeditation <= 10 then '1-10'
                when summaryByRange.factor = 'meditation' and avgMeditation > 10 and avgMeditation <= 20 then '11-20'
                when summaryByRange.factor = 'meditation' and avgMeditation > 20 and avgMeditation <= 30 then '21-30'
                when summaryByRange.factor = 'meditation' and avgMeditation > 30 and avgMeditation <= 40 then '31-40'
                when summaryByRange.factor = 'meditation' and avgMeditation > 40 and avgMeditation <= 50 then '41-50'
                when summaryByRange.factor = 'meditation' and avgMeditation > 50 and avgMeditation <= 60 then '51-60'
                when summaryByRange.factor = 'meditation' and avgMeditation > 60 and avgMeditation <= 70 then '61-70'
                when summaryByRange.factor = 'meditation' and avgMeditation > 70 and avgMeditation <= 80 then '71-80'
                when summaryByRange.factor = 'meditation' and avgMeditation > 80 and avgMeditation <= 90 then '81-90'
                when summaryByRange.factor = 'meditation' and avgMeditation > 90 and avgMeditation <= 100 then '91-100'
                when summaryByRange.factor = 'attention' and avgAttention >= 1 and avgAttention <= 10 then '1-10'
                when summaryByRange.factor = 'attention' and avgAttention > 10 and avgAttention <= 20 then '11-20'
                when summaryByRange.factor = 'attention' and avgAttention > 20 and avgAttention <= 30 then '21-30'
                when summaryByRange.factor = 'attention' and avgAttention > 30 and avgAttention <= 40 then '31-40'
                when summaryByRange.factor = 'attention' and avgAttention > 40 and avgAttention <= 50 then '41-50'
                when summaryByRange.factor = 'attention' and avgAttention > 50 and avgAttention <= 60 then '51-60'
                when summaryByRange.factor = 'attention' and avgAttention > 60 and avgAttention <= 70 then '61-70'
                when summaryByRange.factor = 'attention' and avgAttention > 70 and avgAttention <= 80 then '71-80'
                when summaryByRange.factor = 'attention' and avgAttention > 80 and avgAttention <= 90 then '81-90'
                when summaryByRange.factor = 'attention' and avgAttention > 90 and avgAttention <= 100 then '91-100'
                when summaryByRange.factor = 'ratio' and amRatio < .5 then '<.50'
                when summaryByRange.factor = 'ratio' and amRatio >= .5 and amRatio < .6 then '.50-.59'
                when summaryByRange.factor = 'ratio' and amRatio >= .6 and amRatio < .7 then '.60-.69'
                when summaryByRange.factor = 'ratio' and amRatio >= .7 and amRatio < .8 then '.70-.79'
                when summaryByRange.factor = 'ratio' and amRatio >= .8 and amRatio < .9 then '.80-.89'
                when summaryByRange.factor = 'ratio' and amRatio >= .9 and amRatio < 1 then '.90-.99'
                when summaryByRange.factor = 'ratio' and amRatio >= 1 and amRatio < 1.1 then '1.0-1.1'
                when summaryByRange.factor = 'ratio' and amRatio >= 1.1 and amRatio < 1.2 then '1.1-1.2'
                when summaryByRange.factor = 'ratio' and amRatio >= 1.2 then '>1.2'
            else ''
            end as "range",
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
        where brainRecords > 0
        group by
            case
                --when when avgMeditation = 0 then '0'
                when summaryByRange.factor = 'meditation' and avgMeditation >= 1 and avgMeditation <= 10 then '1-10'
                when summaryByRange.factor = 'meditation' and avgMeditation > 10 and avgMeditation <= 20 then '11-20'
                when summaryByRange.factor = 'meditation' and avgMeditation > 20 and avgMeditation <= 30 then '21-30'
                when summaryByRange.factor = 'meditation' and avgMeditation > 30 and avgMeditation <= 40 then '31-40'
                when summaryByRange.factor = 'meditation' and avgMeditation > 40 and avgMeditation <= 50 then '41-50'
                when summaryByRange.factor = 'meditation' and avgMeditation > 50 and avgMeditation <= 60 then '51-60'
                when summaryByRange.factor = 'meditation' and avgMeditation > 60 and avgMeditation <= 70 then '61-70'
                when summaryByRange.factor = 'meditation' and avgMeditation > 70 and avgMeditation <= 80 then '71-80'
                when summaryByRange.factor = 'meditation' and avgMeditation > 80 and avgMeditation <= 90 then '81-90'
                when summaryByRange.factor = 'meditation' and avgMeditation > 90 and avgMeditation <= 100 then '91-100'
                when summaryByRange.factor = 'attention' and avgAttention >= 1 and avgAttention <= 10 then '1-10'
                when summaryByRange.factor = 'attention' and avgAttention > 10 and avgAttention <= 20 then '11-20'
                when summaryByRange.factor = 'attention' and avgAttention > 20 and avgAttention <= 30 then '21-30'
                when summaryByRange.factor = 'attention' and avgAttention > 30 and avgAttention <= 40 then '31-40'
                when summaryByRange.factor = 'attention' and avgAttention > 40 and avgAttention <= 50 then '41-50'
                when summaryByRange.factor = 'attention' and avgAttention > 50 and avgAttention <= 60 then '51-60'
                when summaryByRange.factor = 'attention' and avgAttention > 60 and avgAttention <= 70 then '61-70'
                when summaryByRange.factor = 'attention' and avgAttention > 70 and avgAttention <= 80 then '71-80'
                when summaryByRange.factor = 'attention' and avgAttention > 80 and avgAttention <= 90 then '81-90'
                when summaryByRange.factor = 'attention' and avgAttention > 90 and avgAttention <= 100 then '91-100'
                when summaryByRange.factor = 'ratio' and amRatio < .5 then '<.50'
                when summaryByRange.factor = 'ratio' and amRatio >= .5 and amRatio < .6 then '.50-.59'
                when summaryByRange.factor = 'ratio' and amRatio >= .6 and amRatio < .7 then '.60-.69'
                when summaryByRange.factor = 'ratio' and amRatio >= .7 and amRatio < .8 then '.70-.79'
                when summaryByRange.factor = 'ratio' and amRatio >= .8 and amRatio < .9 then '.80-.89'
                when summaryByRange.factor = 'ratio' and amRatio >= .9 and amRatio < 1 then '.90-.99'
                when summaryByRange.factor = 'ratio' and amRatio >= 1 and amRatio < 1.1 then '1.0-1.1'
                when summaryByRange.factor = 'ratio' and amRatio >= 1.1 and amRatio < 1.2 then '1.1-1.2'
                when summaryByRange.factor = 'ratio' and amRatio >= 1.2 then '>1.2'
            else ''
        end
    }';
end summaryByRange;