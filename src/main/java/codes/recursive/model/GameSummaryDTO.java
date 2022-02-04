package codes.recursive.model;

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

    @Nullable
    private String grouping;
    private Integer totalGames;
    private BigDecimal avgAttention;
    private BigDecimal avgMeditation;
    private Integer totalKills;
    private BigDecimal avgKills;
    private BigDecimal avgDeaths;
    private Integer maxKills;
    private Integer maxEliminations;
    private Integer minKills;
    private Integer maxDeaths;
    private Integer minDeaths;
    private Integer maxAssists;
    private Integer minAssists;
    private BigDecimal maxKdRatio;
    private Integer totalAssists;
    private Integer totalDeaths;
    private BigDecimal avgKdRatio;
    private BigDecimal totalKdRatio;
    private BigDecimal totalEdRatio;
    private Integer totalWins;
    private Integer totalLosses;
    private BigDecimal wlRatio;
    private Integer totalShotsLanded;
    private Integer totalShotsMissed;
    private Integer totalShotsFired;
    private BigDecimal averageAccuracy;
    private BigDecimal totalAccuracy;
    private Integer totalDamageDone;
    private Integer totalDamageTaken;
    private Integer longestStreak;
    private BigDecimal avgScore;
    private BigDecimal totalScorePerMinute;
    private BigDecimal avgScorePerMinute;
    private BigDecimal totalDistanceTraveled;
    private BigDecimal avgPctTimeMoving;
    private BigDecimal avgSpeedDuringMatch;
}
