package codes.recursive.model;

import codes.recursive.util.TimeFormatter;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class GameDetailDTO {
    private String game;
    private Boolean isHighlighted;
    private Boolean isDistracted;
    private BigInteger utcStartSeconds;
    private BigInteger utcEndSeconds;
    private String map;
    private BigInteger matchID;
    private String playlistName;
    private String gameType;
    private String result;
    private Integer team1Score;
    private Integer team2Score;
    private Boolean isPresentAtEnd;
    private Integer kills;
    private Integer rankAtEnd;
    private BigDecimal averageSpeedDuringMatch;
    private BigDecimal accuracy;
    private Integer score;
    private Integer headshots;
    private Integer assists;
    private BigDecimal scorePerMinute;
    private BigDecimal killsPerMinute;
    private BigInteger distanceTraveled;
    private Integer deaths;
    private Integer shotsLanded;
    private Integer shotsMissed;
    private BigDecimal kdRatio;
    private BigDecimal edRatio;
    private Integer prestigeAtEnd;
    private Integer timePlayed;
    private Integer executions;
    private Integer suicides;
    private BigDecimal percentTimeMoving;
    private Integer longestStreak;
    private Integer damageDone;
    private Integer shots;
    private Integer shotsFired;
    private Integer damageTaken;
    private String operator;
    private String platform;
    private String team;
    private Timestamp matchStart;
    private Timestamp matchEnd;
    private BigDecimal amRatio;
    private Integer brainRecords;
    private BigInteger totalAttention;
    private BigInteger totalMeditation;
    private BigInteger totalDelta;
    private BigInteger totalTheta;
    private BigInteger totalLowAlpha;
    private BigInteger totalHighAlpha;
    private BigInteger totalLowBeta;
    private BigInteger totalHighBeta;
    private BigInteger totalLowGamma;
    private Integer avgAttention;
    private Integer avgMeditation;
    private Integer avgDelta;
    private Integer avgTheta;
    private Integer avgLowAlpha;
    private Integer avgHighAlpha;
    private Integer avgLowBeta;
    private Integer avgHighBeta;
    private Integer avgLowGamma;
    private Integer avgHighGamma;
    private Boolean isDistractedBrain;

    public String getFormattedStartDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d '@' hh:mma");
        return formatter.format(matchStart);
    }
    public String getFormattedEndDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d '@' hh:mma");
        return formatter.format(matchEnd);
    }

    public String getFriendlyTimePlayed() {
        return TimeFormatter.formatTimeDurationShort(timePlayed);
    }
}
