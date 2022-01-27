package codes.recursive.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Introspected
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {
    @JsonProperty("utcStartSeconds")
    public Long utcStartSeconds;
    @JsonProperty("utcEndSeconds")
    public Long utcEndSeconds;
    @JsonProperty("grouping")
    public String map;
    @JsonProperty("mode")
    public String mode;
    @JsonProperty("matchID")
    public BigInteger matchID;
    @JsonProperty("duration")
    public Long duration;
    @JsonProperty("playlistName")
    public String playlistName;
    @JsonProperty("version")
    public Integer version;
    @JsonProperty("gameType")
    public String gameType;
    @JsonProperty("result")
    public String result;
    @JsonProperty("winningTeam")
    public String winningTeam;
    @JsonProperty("gameBattle")
    public Boolean gameBattle;
    @JsonProperty("team1Score")
    public Integer team1Score;
    @JsonProperty("team2Score")
    public Integer team2Score;
    @JsonProperty("isPresentAtEnd")
    public Boolean isPresentAtEnd;
    @JsonProperty("playerStats")
    public PlayerStats playerStats;
    @JsonProperty("player")
    public Player player;

    @JsonCreator
    public Match(@JsonProperty("utcStartSeconds") Long utcStartSeconds,
                 @JsonProperty("utcEndSeconds") Long utcEndSeconds,
                 @JsonProperty("grouping") String map,
                 @JsonProperty("mode") String mode,
                 @JsonProperty("matchID") BigInteger matchID,
                 @JsonProperty("duration") Long duration,
                 @JsonProperty("playlistName") String playlistName,
                 @JsonProperty("version") Integer version,
                 @JsonProperty("gameType") String gameType,
                 @JsonProperty("result") String result,
                 @JsonProperty("winningTeam") String winningTeam,
                 @JsonProperty("gameBattle") Boolean gameBattle,
                 @JsonProperty("team1Score") Integer team1Score,
                 @JsonProperty("team2Score") Integer team2Score,
                 @JsonProperty("isPresentAtEnd") Boolean isPresentAtEnd,
                 @JsonProperty("playerStats") PlayerStats playerStats,
                 @JsonProperty("player") Player player) {
        this.utcStartSeconds = utcStartSeconds;
        this.utcEndSeconds = utcEndSeconds;
        this.map = map;
        this.mode = mode;
        this.matchID = matchID;
        this.duration = duration;
        this.playlistName = playlistName;
        this.version = version;
        this.gameType = gameType;
        this.result = result;
        this.winningTeam = winningTeam;
        this.gameBattle = gameBattle;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.isPresentAtEnd = isPresentAtEnd;
        this.playerStats = playerStats;
        this.player = player;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    public ZonedDateTime getMatchStart() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.utcStartSeconds), ZoneId.of("America/New_York"));
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ssZ")
    public ZonedDateTime getMatchEnd() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.utcEndSeconds), ZoneId.of("America/New_York"));

    }
}