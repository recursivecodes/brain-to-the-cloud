package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerStats {
    @JsonProperty("kills")
    public Integer kills;
    @JsonProperty("rankAtEnd")
    public Integer rankAtEnd;
    @JsonProperty("averageSpeedDuringMatch")
    public Double averageSpeedDuringMatch;
    @JsonProperty("accuracy")
    public Double accuracy;
    @JsonProperty("shotsLanded")
    public Integer shotsLanded;
    @JsonProperty("score")
    public Integer score;
    @JsonProperty("headshots")
    public Integer headshots;
    @JsonProperty("utcConnectTimeS")
    public Long utcConnectTimeS;
    @JsonProperty("assists")
    public Integer assists;
    @JsonProperty("scorePerMinute")
    public Double scorePerMinute;
    @JsonProperty("distanceTraveled")
    public Double distanceTraveled;
    @JsonProperty("deaths")
    public Integer deaths;
    @JsonProperty("shotsMissed")
    public Integer shotsMissed;
    @JsonProperty("kdRatio")
    public Double kdRatio;
    @JsonProperty("prestigeAtEnd")
    public Integer prestigeAtEnd;
    @JsonProperty("timePlayed")
    public Integer timePlayed;
    @JsonProperty("executions")
    public Integer executions;
    @JsonProperty("suicides")
    public Integer suicides;
    @JsonProperty("percentTimeMoving")
    public Double percentTimeMoving;
    @JsonProperty("utcDisconnectTimeS")
    public Long utcDisconnectTimeS;
    @JsonProperty("longestStreak")
    public Integer longestStreak;
    @JsonProperty("damageDone")
    public Integer damageDone;
    @JsonProperty("shots")
    public Integer shots;
    @JsonProperty("shotsFired")
    public Integer shotsFired;
    @JsonProperty("damageTaken")
    public Integer damageTaken;

    @JsonCreator
    public PlayerStats(@JsonProperty("kills") Integer kills,
                       @JsonProperty("rankAtEnd") Integer rankAtEnd,
                       @JsonProperty("averageSpeedDuringMatch") Double averageSpeedDuringMatch,
                       @JsonProperty("accuracy") Double accuracy,
                       @JsonProperty("shotsLanded") Integer shotsLanded,
                       @JsonProperty("score") Integer score,
                       @JsonProperty("headshots") Integer headshots,
                       @JsonProperty("utcConnectTimeS") Long utcConnectTimeS,
                       @JsonProperty("assists") Integer assists,
                       @JsonProperty("scorePerMinute") Double scorePerMinute,
                       @JsonProperty("distanceTraveled") Double distanceTraveled,
                       @JsonProperty("deaths") Integer deaths,
                       @JsonProperty("shotsMissed") Integer shotsMissed,
                       @JsonProperty("kdRatio") Double kdRatio,
                       @JsonProperty("prestigeAtEnd") Integer prestigeAtEnd,
                       @JsonProperty("timePlayed") Integer timePlayed,
                       @JsonProperty("executions") Integer executions,
                       @JsonProperty("suicides") Integer suicides,
                       @JsonProperty("percentTimeMoving") Double percentTimeMoving,
                       @JsonProperty("utcDisconnectTimeS") Long utcDisconnectTimeS,
                       @JsonProperty("longestStreak") Integer longestStreak,
                       @JsonProperty("damageDone") Integer damageDone,
                       @JsonProperty("shots") Integer shots,
                       @JsonProperty("shotsFired") Integer shotsFired,
                       @JsonProperty("damageTaken") Integer damageTaken) {
        this.kills = kills;
        this.rankAtEnd = rankAtEnd;
        this.averageSpeedDuringMatch = averageSpeedDuringMatch;
        this.accuracy = accuracy;
        this.shotsLanded = shotsLanded;
        this.score = score;
        this.headshots = headshots;
        this.utcConnectTimeS = utcConnectTimeS;
        this.assists = assists;
        this.scorePerMinute = scorePerMinute;
        this.distanceTraveled = distanceTraveled;
        this.deaths = deaths;
        this.shotsMissed = shotsMissed;
        this.kdRatio = kdRatio;
        this.prestigeAtEnd = prestigeAtEnd;
        this.timePlayed = timePlayed;
        this.executions = executions;
        this.suicides = suicides;
        this.percentTimeMoving = percentTimeMoving;
        this.utcDisconnectTimeS = utcDisconnectTimeS;
        this.longestStreak = longestStreak;
        this.damageDone = damageDone;
        this.shots = shots;
        this.shotsFired = shotsFired;
        this.damageTaken = damageTaken;
    }
}
