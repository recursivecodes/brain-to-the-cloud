drop view vw_brain_session_details;

create view vw_brain_session_details
as
select 
    bs.id,
    b.attention,
    b.meditation,
    b.delta,
    b.theta,
    b.low_alpha as lowAlpha,
    b.high_alpha as highAlpha,
    b.low_beta as lowBeta,
    b.high_beta as highBeta,
    b.low_gamma as lowGamma,
    b.high_gamma as highGamma,
    b.created_on as createdOn
from brain_session bs
    left outer join brain b
        on bs.started_at <= b.created_on     
        and bs.ended_at >= b.created_on
order by b.created_on;       