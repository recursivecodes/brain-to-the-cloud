select id, 
    tsToIso(epochToLocalTimestamp(g.match.utcStartSeconds)) matchStart,
    tsToIso(epochToLocalTimestamp(g.match.utcEndSeconds)) matchEnd,
    g.match.map
from game g;

select epochToLocalTimestamp(1638499635)
from dual;

select *
from brain
where toLocalTimestamp(created_on) between epochToLocalTimestamp(1638899958) and epochToLocalTimestamp(1638899958+100000);

create or replace function epochToLocalTimestamp
(
    epoch in number
) 
return TIMESTAMP WITH TIME ZONE
is
    new_date TIMESTAMP WITH TIME ZONE;
begin
    select TO_TIMESTAMP_TZ('1970-01-01 00:00:00 -00:00', 'YYYY-MM-DD HH24:MI:SS.FF TZH:TZM') at time zone 'America/New_York' + numtodsinterval(EPOCH, 'SECOND')
    into new_date
    from dual;
  return new_date;
end epochToLocalTimestamp;

create or replace function toLocalTimestamp 
(
    dt in date
) 
return timestamp with time zone
is
    new_date timestamp with time zone;
begin
    select FROM_TZ( CAST( dt AS TIMESTAMP ), 'UTC' ) AT TIME ZONE 'America/New_York'
    into new_date
    from dual;
  return new_date;
end toLocalTimestamp;

create or replace function toLocalDate 
(
    dt in date
) 
return date
is
    new_date date;
begin
    select FROM_TZ( CAST( dt AS TIMESTAMP ), 'UTC' ) AT TIME ZONE 'America/New_York'
    into new_date
    from dual;
  return new_date;
end toLocalDate;

create or replace function tsToIso 
(
    inDate in timestamp with time zone
) 
return varchar2
is
    formattedDate varchar2(100);
begin
    select to_char( inDate, 'YYYY-MM-DD"T"HH:mi:sstzh:tzm')
    into formattedDate
    from dual;
  return formattedDate;
end tsToIso;

