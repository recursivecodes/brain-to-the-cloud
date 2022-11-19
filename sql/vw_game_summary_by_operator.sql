drop view vw_game_summary_by_operator;

create view vw_game_summary_by_operator
    as  
select
    game,
    operator as grouping,
    count("id") as totalGames,
    sum(kills) as totalKills, 
    cast( avg(kills) as number(18,2)) as avgKills,
    cast( avg(deaths) as number(18,2)) as avgDeaths,
    max(kills) as maxKills,
    max(kills+assists) as maxEliminations,
    min(kills) as minKills,
    max(deaths) as maxDeaths,
    min(deaths) as minDeaths,
    max(assists) as maxAssists,
    min(assists) as minAssists,
    max(kdRatio) as maxKdRatio,
    sum(assists) as totalAssists, 
    sum(deaths) as totalDeaths,
    cast(avg(kdRatio) as number(18,2)) as avgKdRatio,
    cast(case when sum(deaths) > 0 then sum(kills) / sum(deaths) else 0 end as number(18,2)) as totalKdRatio,
    cast(case when sum(deaths) > 0 then (sum(kills) + sum(assists)) / sum(deaths) else 0 end as number(18,2)) as totalEdRatio,
    
    sum( case when "result" = 'win' then 1 else 0 end ) as totalWins,
    sum( case when "result" = 'loss' then 1 else 0 end ) as totalLosses,
    cast(
        case 
            when sum( case when "result" = 'loss' then 1 else 0 end ) > 0 
            then sum( case when "result" = 'win' then 1 else 0 end ) / sum( case when "result" = 'loss' then 1 else 0 end ) 
        else 0 end as number(18,2)
    )    
    as wlRatio,
        
    sum(shotsLanded) as totalShotsLanded,
    sum(shotsMissed) as totalShotsMissed,
    sum(shotsFired) as totalShotsFired,
    cast(avg(accuracy) as number(18,2)) as averageAccuracy,
    cast(case when sum(shotsFired) > 0 then sum(shotsLanded) / sum(shotsFired) else 0 end as number(18,2)) as totalAccuracy,
    
    sum(damageDone) as totalDamageDone,
    sum(damageTaken) as totalDamageTaken,
    
    max(to_number(longestStreak)) as longestStreak,
    
    cast(avg(score) as number(18,2)) as avgScore,
    cast(case when sum(timePlayed) > 0 then sum(score) / (sum(timePlayed) / 60) else 0 end as number(18,2)) as totalScorePerMinute,
    cast(avg(scorePerMinute) as number(18,2)) as avgScorePerMinute,
    cast(sum(timePlayed) as number(18,2)) as totalTimePlayed,
    cast(sum(distanceTraveled) as number(18,2)) as totalDistanceTraveled,
    cast(avg(percentTimeMoving) as number(18,2)) as avgPctTimeMoving,
    cast(avg(averageSpeedDuringMatch) as number(18,2)) as avgSpeedDuringMatch
from mv_game_details
group by game, operator;