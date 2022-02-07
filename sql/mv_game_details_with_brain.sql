drop materialized view mv_game_details_with_brain;

create materialized view mv_game_details_with_brain
    refresh complete
    start with sysdate
    next sysdate + interval '1' hour
    as
select
    g.id as "id",
    g.is_highlighted as isHighlighted,
    g.is_distracted as isDistracted,
    g.match.utcStartSeconds as utcStartSeconds,
    g.match.utcEndSeconds as utcEndSeconds,
    g.match.map as map,
    g.match."mode" as "mode",
    g.match.matchID as matchID,
    cast(g.match.duration as number(18,0)) as "duration",
    g.match.playlistName as playlistName,
    g.match.version as "version",
    g.match.gameType as gameType,
    g.match.result as "result",
    cast(g.match.team1Score as number(18,0)) as team1Score,
    cast(g.match.team2Score as number(18,0)) as team2Score,
    g.match.isPresentAtEnd as isPresentAtEnd,
    cast(g.match.playerStats.kills as number(18,0)) as kills,
    cast(g.match.playerStats.rankAtEnd as number(18,0)) as rankAtEnd,
    cast(g.match.playerStats.averageSpeedDuringMatch as number(18,2)) as averageSpeedDuringMatch,
    cast(g.match.playerStats.accuracy as number(18,2)) as accuracy,
    cast(g.match.playerStats.score as number(18,0)) as score,
    cast(g.match.playerStats.headshots as number(18,0)) as headshots,
    cast(g.match.playerStats.assists as number(18,0)) as assists,
    cast(g.match.playerStats.scorePerMinute as number(18,2)) as scorePerMinute,
    cast(g.match.playerStats.distanceTraveled as number(18,0)) as distanceTraveled,
    cast(g.match.playerStats.deaths as number(18,0)) as deaths,
    cast(g.match.playerStats.shotsLanded as number(18,0)) as shotsLanded,
    cast(g.match.playerStats.shotsMissed as number(18,0)) as shotsMissed,
    cast(g.match.playerStats.kdRatio as number(18,2)) as kdRatio,
    cast(g.match.playerStats.prestigeAtEnd as number(18,0)) as prestigeAtEnd,
    cast(g.match.playerStats.timePlayed as number(18,2)) as timePlayed,
    cast(g.match.playerStats.executions as number(18,0)) as executions,
    cast(g.match.playerStats.suicides as number(18,0)) as suicides,
    cast(g.match.playerStats.percentTimeMoving as number(18,2)) as percentTimeMoving,
    cast(g.match.playerStats.longestStreak as number(18,0)) as longestStreak,
    cast(g.match.playerStats.damageDone as number(18,0)) as damageDone,
    cast(g.match.playerStats.shots as number(18,0)) as shots,
    cast(g.match.playerStats.shotsFired as number(18,0)) as shotsFired,
    cast(g.match.playerStats.damageTaken as number(18,0)) as damageTaken,
    g.match.player.operator as operator,
    g.match.player.platform as platform,
    g.match.player.team as team,
    g.match.matchStart as matchStart,
    g.match.matchEnd as matchEnd,
    cast( case when sum(b.attention) > 0 then sum(b.attention) / sum(b.meditation) else 0 end as number(18,2) ) as amRatio,

    count(b.id) as brainRecords,
    sum(b.attention) as totalAttention,
    sum(b.meditation) as totalMeditation,
    sum(b.delta) as totalDelta,
    sum(b.theta) as totalTheta,
    sum(b.low_alpha) as totalLowAlpha,
    sum(b.high_alpha) as totalHighAlpha,
    sum(b.low_beta) as totalLowBeta,
    sum(b.high_beta) as totalHighBeta,
    sum(b.low_gamma) as totalLowGamma,
    sum(b.high_gamma) as totalHighGamma,
    
    avg(case when b.signal_strength = 100 then b.attention else null end) as avgAttention,
    avg(case when b.signal_strength = 100 then b.meditation else null end) as avgMeditation,
    avg(case when b.signal_strength = 100 then b.delta else null end) as avgDelta,
    avg(case when b.signal_strength = 100 then b.theta else null end) as avgTheta,
    avg(case when b.signal_strength = 100 then b.low_alpha else null end) as avgLowAlpha,
    avg(case when b.signal_strength = 100 then b.high_alpha else null end) as avgHighAlpha,
    avg(case when b.signal_strength = 100 then b.low_beta else null end) as avgLowBeta,
    avg(case when b.signal_strength = 100 then b.high_beta else null end) as avgHighBeta,
    avg(case when b.signal_strength = 100 then b.low_gamma else null end) as avgLowGamma,
    avg(case when b.signal_strength = 100 then b.high_gamma else null end) as avgHighGamma,
    max(b.is_distracted) as isDistractedBrain
from game g
left outer join brain b
    on  toLocalTimestamp(b.created_on) >= epochToLocalTimestamp(g.match.utcStartSeconds)
    and toLocalTimestamp(b.created_on) <= epochToLocalTimestamp(g.match.utcEndSeconds)
--where b.id is not null
group by
    g.id,
    g.is_highlighted,
    g.is_distracted,
    to_char(g.match),
    g.match.utcStartSeconds,
    g.match.utcEndSeconds,
    g.match.map,
    g.match."mode",
    g.match.matchID,
    g.match.duration,
    g.match.playlistName,
    g.match.version,
    g.match.gameType,
    g.match.result,
    g.match.team1Score,
    g.match.team2Score,
    g.match.isPresentAtEnd,
    g.match.playerStats.kills,
    g.match.playerStats.rankAtEnd,
    g.match.playerStats.averageSpeedDuringMatch,
    g.match.playerStats.accuracy,
    g.match.playerStats.score,
    g.match.playerStats.headshots,
    g.match.playerStats.assists,
    g.match.playerStats.scorePerMinute,
    g.match.playerStats.distanceTraveled,
    g.match.playerStats.deaths,
    g.match.playerStats.shotsLanded,
    g.match.playerStats.shotsMissed,
    g.match.playerStats.kdRatio,
    g.match.playerStats.prestigeAtEnd,
    g.match.playerStats.timePlayed,
    g.match.playerStats.executions,
    g.match.playerStats.suicides,
    g.match.playerStats.percentTimeMoving,
    g.match.playerStats.longestStreak,
    g.match.playerStats.damageDone,
    g.match.playerStats.shots,
    g.match.playerStats.shotsFired,
    g.match.playerStats.damageTaken,
    g.match.player.operator,
    g.match.player.platform,
    g.match.player.team,
    g.match.matchStart,
    g.match.matchEnd