drop view vw_brain_session_summary;

create view vw_brain_session_summary
as
select 
    bs.id,
    bs.name,
    bs.notes,
    bs.started_at as startedAt,
    bs.ended_at as endedAt,
    cast( avg(case when b.signal_strength = 100 then b.attention else null end) as number(18,2)) as avgAttention,
    cast( avg(case when b.signal_strength = 100 then b.meditation else null end) as number(18,2)) as avgMeditation,
    cast( avg(case when b.signal_strength = 100 then b.delta else null end) as number(18,2)) as avgDelta,
    cast( avg(case when b.signal_strength = 100 then b.theta else null end) as number(18,2)) as avgTheta,
    cast( avg(case when b.signal_strength = 100 then b.low_alpha else null end) as number(18,2)) as avgLowAlpha,
    cast( avg(case when b.signal_strength = 100 then b.high_alpha else null end) as number(18,2)) as avgHighAlpha,
    cast( avg(case when b.signal_strength = 100 then b.low_beta else null end) as number(18,2)) as avgLowBeta,
    cast( avg(case when b.signal_strength = 100 then b.high_beta else null end) as number(18,2)) as avgHighBeta,
    cast( avg(case when b.signal_strength = 100 then b.low_gamma else null end) as number(18,2)) as avgLowGamma,
    cast( avg(case when b.signal_strength = 100 then b.high_gamma else null end) as number(18,2)) as avgHighGamma
from brain_session bs
    left outer join brain b
        on bs.started_at <= b.created_on     
        and bs.ended_at >= b.created_on       
group by 
    bs.id,
    bs.name,
    bs.notes,
    bs.started_at,
    bs.ended_at
order by bs.started_at;   