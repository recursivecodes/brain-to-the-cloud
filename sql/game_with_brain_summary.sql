select 
    g.id, 
    to_char(g.match) as match, 
    g.is_highlighted as isHighlighted, 
    g.created_on as createdOn, 
    
    avg(b.attention) as avgAttention,
    avg(b.meditation) as avgMeditation,
    avg(b.delta) as avgDelta,
    avg(b.theta) as avgTheta,
    avg(b.low_alpha) as avgLowAlpha,
    avg(b.high_alpha) as avgHighAlpha,
    avg(b.low_beta) as avgLowBeta,
    avg(b.high_beta) as avgHighBeta,
    avg(b.low_gamma) as avgLowGamma,
    avg(b.high_gamma) as avgHighGamma,
    max(b.is_distracted) as isDistracted,
    
    g.match.playerStats.score / avg(b.attention) as scoreAttention
    
from game g
    left outer join brain b
        on  toLocalTimestamp(b.created_on) >= epochToLocalTimestamp(g.match.utcStartSeconds)
        and toLocalTimestamp(b.created_on) <= epochToLocalTimestamp(g.match.utcEndSeconds)      
where g.id = 27        
group by 
    g.id, 
    to_char(g.match), 
    g.is_highlighted, 
    g.match.utcStartSeconds, 
    g.created_on,
    g.match.playerStats.score
having case when count(b.id) > 0 then 1 else 0 end > 0            
order by g.match.utcStartSeconds desc        

/*
{"utcStartSeconds":1639112752,"utcEndSeconds":1639113427,"map":"mp_radar","mode":"dom","matchID":5187719363727371596,"duration":675000,"playlistName":"PLAYLIST/DOM_NAME","version":1,"gameType":"mp","result":"loss","winningTeam":null,"gameBattle":false,"team1Score":0,"team2Score":0,"isPresentAtEnd":true,"playerStats":{"kills":34,"rankAtEnd":54,"averageSpeedDuringMatch":168.03195,"accuracy":0.155,"shotsLanded":93,"score":5200,"headshots":2,"utcConnectTimeS":1639112736,"assists":2,"scorePerMinute":421.6216216216216,"distanceTraveled":8743.007,"deaths":24,"shotsMissed":507,"kdRatio":1.4166666666666667,"prestigeAtEnd":3,"timePlayed":740,"executions":0,"suicides":0,"percentTimeMoving":95.63253,"utcDisconnectTimeS":1639113476,"longestStreak":8,"damageDone":3525,"shots":600,"shotsFired":600,"damageTaken":3041},"player":{"mostKilled":"none","nemesis":"none","operator":"solange","operatorExecution":"execution_mp_universal_ref_variant_18","operatorSkinId":33585715,"platform":"xbl","team":"axis"},"matchStart":"2021-12-10 00:05:52-0500","matchEnd":"2021-12-10 00:17:07-0500"}
*/