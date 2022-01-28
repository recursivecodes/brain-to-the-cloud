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
    @Nullable
    private String grouping;
    private Integer totalGames;
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
