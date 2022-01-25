package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import javax.annotation.Nullable;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class RangeSummaryDTO {
    private String range;
    private Integer totalWins;
    private Integer totalLosses;
    private Double wlRatio;
    private Integer kills;
    private Integer deaths;
    private Double kdRatio;
    private Double edRatio;
    private Integer score;
    private Double avgScore;
    private Integer timePlayed;
    private Double scorePerMinute;
    @Nullable
    private Double avgAttention;
    @Nullable
    private Double avgMeditation;
}
