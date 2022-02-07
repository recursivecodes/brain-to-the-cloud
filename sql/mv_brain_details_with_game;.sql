drop materialized view mv_brain_details_with_game;

create materialized view mv_brain_details_with_game
    refresh complete
    start with sysdate
    next sysdate + interval '1' hour
    as
select 
    g.id as game_id,
    b.id as "id",
    b.signal_strength,
    b.attention,
    b.meditation,
    b.delta,
    b.theta,
    b.low_alpha,
    b.high_alpha,
    b.low_beta,
    b.high_beta,
    b.low_gamma,
    b.high_gamma,
    b.is_distracted,
    b.created_on
from game g
left outer join brain b
    on  toLocalTimestamp(b.created_on) >= epochToLocalTimestamp(g.match.utcStartSeconds)
    and toLocalTimestamp(b.created_on) <= epochToLocalTimestamp(g.match.utcEndSeconds);    