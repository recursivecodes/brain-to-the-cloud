create or replace function timeMovingRange(withBrain in number) return varchar2 sql_macro is
begin
    return q'{
        select
        case
            --when percentTimeMoving = 0 then '0'
            when percentTimeMoving >= 1 and percentTimeMoving <= 10 then '1-10'
            when percentTimeMoving > 10 and percentTimeMoving <= 20 then '11-20'
            when percentTimeMoving > 20 and percentTimeMoving <= 30 then '21-30'
            when percentTimeMoving > 30 and percentTimeMoving <= 40 then '31-40'
            when percentTimeMoving > 40 and percentTimeMoving <= 50 then '41-50'
            when percentTimeMoving > 50 and percentTimeMoving <= 60 then '51-60'
            when percentTimeMoving > 60 and percentTimeMoving <= 70 then '61-70'
            when percentTimeMoving > 70 and percentTimeMoving <= 80 then '71-80'
            when percentTimeMoving > 80 and percentTimeMoving <= 90 then '81-90'
            when percentTimeMoving > 90 and percentTimeMoving <= 100 then '91-100'
            else ''
        end as timeRange,
        sum(brainRecords) as brainRecords,
        sum(kills) as kills,
        sum(assists) as assists,
        sum(deaths) as deaths,
        sum(score) as score,
        avg(score) as avgScore,
        avg(accuracy) as avgAccuracy,
        sum(timePlayed) as timePlayed,
        sum(timePlayed/60) as minutesPlayed,
        sum( case when "result" = 'loss' then 1 else 0 end ) as totalLosses,
        sum( case when "result" = 'win' then 1 else 0 end ) as totalWins,
        avg(avgAttention) as avgAttention,
        avg(avgMeditation) as avgMeditation
        from mv_game_details_with_brain
        where (timeMovingRange.withBrain = 1 and brainRecords > 0) OR (timeMovingRange.withBrain = 0)
        group by
            case
                --when percentTimeMoving = 0 then '0'
                when percentTimeMoving >= 1 and percentTimeMoving <= 10 then '1-10'
                when percentTimeMoving > 10 and percentTimeMoving <= 20 then '11-20'
                when percentTimeMoving > 20 and percentTimeMoving <= 30 then '21-30'
                when percentTimeMoving > 30 and percentTimeMoving <= 40 then '31-40'
                when percentTimeMoving > 40 and percentTimeMoving <= 50 then '41-50'
                when percentTimeMoving > 50 and percentTimeMoving <= 60 then '51-60'
                when percentTimeMoving > 60 and percentTimeMoving <= 70 then '61-70'
                when percentTimeMoving > 70 and percentTimeMoving <= 80 then '71-80'
                when percentTimeMoving > 80 and percentTimeMoving <= 90 then '81-90'
                when percentTimeMoving > 90 and percentTimeMoving <= 100 then '91-100'
                else ''
            end}';
end timeMovingRange;            