select * FROM Game g where g.id = 56 order by g.match.utcStartSeconds desc;

select * 
from brain order by created_on asc;

select * --id, kills, deaths, sum(totalattention)
from mv_game_details_with_brain;

select *
from mv_game_details_with_brain;

select sysdate from dual; 
select id, attention, meditation, delta, theta, low_alpha, high_alpha, low_beta,high_beta, low_gamma, high_gamma from vw_brain_session_details;

delete from game where id = 474;

select * 
from mv_game_details;

select * from vw_game_summary_by_map;

describe vw_game_summary;

select * 
from vw_game_summary_by_map order by totalEdRatio desc;

select map, totalGames, totalWins, totalLosses, wlRatio
from vw_game_summary_by_map
order by wlRatio desc;

select *
from vw_game_summary;

select * 
from vw_game_summary_by_operator;

select * 
from vw_game_summary_by_mode;

select *
from mv_game_details_with_brain;

select *
from vw_summary_by_attention_range;

select *
from vw_summary_by_meditation_range;

select * 
from vw_summary_by_time_moving order by range desc;

select *
from vw_summary_by_time_moving_with_brain_data;

update game set is_highlighted = 0;

update game set is_highlighted = 0;

select * from brain ib left outer join brain_session bs on bs.started_at <= ib.created_on and bs.ended_at >= ib.created_on;
--where bs.id = :brainSessionId

delete from brain b where b.id in (select ib.id from brain ib left outer join brain_session bs on bs.started_at <= ib.created_on and bs.ended_at >= ib.created_on where bs.id = 22);

select g.id, g.match, g.created_on, g.is_highlighted, g.is_distracted, g.notes, g.match.utcStartSeconds from game g where id in (select mvb."id" from mv_game_details_with_brain mvb where mvb.totalAttention is not null) order by g.match.utcStartSeconds;