select * 
from mv_game_details
where timePlayed < 100
order by utcStartSeconds desc;

select count(1) from brain;

select * from mv_game_details_with_brain where brainRecords > 0;

delete from game where id in (607,608,600);

select id, to_timestamp_tz(g.match.matchStart, 'YYYY-MM-DD HH24:MI:SSTZHTZM')
from game g;

select cast(to_timestamp_tz('2022-02-08 12:37:03-0500', 'YYYY-MM-DD HH24:MI:SSTZHTZM') as date)
from dual;

select sysdate + interval '1' hour from dual;
--09-FEB-22 11.36.11 <--- GMT
select sysdate + (1/24) from dual;
--09-FEB-22 11.36.36 <--- same

select * from mv_game_details_with_brain order by matchStart desc;

select owner, mview_name, last_refresh_type, last_refresh_date
from all_mviews;

select *
from all_scheduler_jobs;

select g.match.playerStats.kills, 
g.match.playerStats.kdRatio,
case when g.match.playerStats.deaths > 0 then (g.match.playerStats.kills + g.match.playerStats.assists) / g.match.playerStats.deaths else 0 end
from game g;

select count(1) from mv_game_details_with_brain;

describe mv_game_details_with_brain;

select * from rangeSummary('ratio');

select * from timeMovingRange(0);

select map, cast(sum(distanceTraveled) / count("id") as number(18,2)) avgDistanceTraveled
from mv_game_details
group by map
order by sum(distanceTraveled) / count("id");


select * from mv_game_details where operator = 'francis' and kills = 63;

select * from vw_game_summary_by_map order by totalEdRatio desc;

select * from summaryByType('operator', 1) order by totalEdRatio desc;

select * from game where id = 495;
delete from game where id = 495;

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