select
    to_char(cast(matchStart as timestamp), 'fmhh:"00" AM'),
    sum(kills) as kills,
    sum(assists) as assists,
    sum(deaths) as deaths,
    sum(score) as score,
    cast(avg(score) as number(18,2)) as avgScore,
    cast(case when sum(timePlayed) > 0 then sum(score) / (sum(timePlayed) / 60) else 0 end as number(18,2)) as totalScorePerMinute,
    cast(case when sum(timePlayed) > 0 then sum(kills) / (sum(timePlayed) / 60) else 0 end as number(18,2)) as totalKillsPerMinute,
    cast(avg(accuracy) as number(18,2)) as avgAccuracy,
    sum(timePlayed) as timePlayed,
    cast(sum(timePlayed/60) as number(18,2)) as minutesPlayed,
    sum( case when "result" = 'loss' then 1 else 0 end ) as totalLosses,
    sum( case when "result" = 'win' then 1 else 0 end ) as totalWins,
    cast(avg(avgAttention) as number(18,2)) as avgAttention,
    cast(avg(avgMeditation) as number(18,2)) as avgMeditation,
    cast(avg(amRatio) as number(18,2)) as avgAmRatio
from mv_game_details_with_brain
where brainRecords > 0
group by
    to_char(cast(matchStart as timestamp), 'fmhh:"00" AM'),
    extract(hour from cast(matchStart as timestamp))
order by
    extract(hour from cast(matchStart as timestamp));

select distinct extract(hour from cast(matchStart as timestamp))
from mv_game_details_with_brain;