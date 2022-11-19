package codes.recursive.model;

import codes.recursive.util.TimeFormatter;
import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.annotation.Nullable;
import java.math.BigDecimal;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class GameSummaryDTO {
    private static String BY_MAP = "byMap";
    private static String BY_MODE = "byMode";
    private static String BY_OPERATOR = "byOperator";
    private static String BY_TEAM = "byTeam";
    private static String BY_MAP_WITH_BRAIN = "byMapWithBrain";
    private static String BY_MODE_WITH_BRAIN = "byModeWithBrain";
    private static String BY_OPERATOR_WITH_BRAIN = "byOperatorWithBrain";
    private static String BY_TEAM_WITH_BRAIN = "byTeamWithBrain";

    private String game;
    @Nullable
    private String grouping;
    private Integer totalGames = 0;
    private BigDecimal avgAttention = BigDecimal.valueOf(0);
    private BigDecimal avgMeditation = BigDecimal.valueOf(0);
    private Integer totalKills = 0;
    private BigDecimal avgKills = BigDecimal.valueOf(0);
    private BigDecimal avgDeaths = BigDecimal.valueOf(0);
    private Integer maxKills = 0;
    private Integer maxEliminations = 0;
    private Integer minKills = 0;
    private Integer maxDeaths = 0;
    private Integer minDeaths = 0;
    private Integer maxAssists = 0;
    private Integer minAssists = 0;
    private BigDecimal maxKdRatio = BigDecimal.valueOf(0);
    private Integer totalAssists = 0;
    private Integer totalDeaths = 0;
    private BigDecimal avgKdRatio = BigDecimal.valueOf(0);
    private BigDecimal totalKdRatio = BigDecimal.valueOf(0);
    private BigDecimal totalEdRatio = BigDecimal.valueOf(0);
    private Integer totalWins = 0;
    private Integer totalLosses = 0;
    private BigDecimal wlRatio = BigDecimal.valueOf(0);
    private Integer totalShotsLanded = 0;
    private Integer totalShotsMissed = 0;
    private Integer totalShotsFired = 0;
    private BigDecimal averageAccuracy = BigDecimal.valueOf(0);
    private BigDecimal totalAccuracy = BigDecimal.valueOf(0);
    private Integer totalDamageDone = 0;
    private Integer totalDamageTaken = 0;
    private Integer longestStreak = 0;
    private BigDecimal avgScore = BigDecimal.valueOf(0);
    private BigDecimal totalScorePerMinute = BigDecimal.valueOf(0);
    private BigDecimal totalKillsPerMinute = BigDecimal.valueOf(0);
    private BigDecimal avgScorePerMinute = BigDecimal.valueOf(0);
    private Integer totalTimePlayed = 0;
    private BigDecimal totalDistanceTraveled = BigDecimal.valueOf(0);
    private BigDecimal avgPctTimeMoving = BigDecimal.valueOf(0);
    private BigDecimal avgSpeedDuringMatch = BigDecimal.valueOf(0);

    public Double getTimePlayedHours() {
        return totalTimePlayed / 3600d;
    }

    public String getTimePlayedFriendly() {
        return TimeFormatter.formatDurationHoursMinutes(totalTimePlayed);
    }
}
