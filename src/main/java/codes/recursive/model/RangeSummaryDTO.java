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
public class RangeSummaryDTO {
    private String range;
    private Integer totalWins;
    private Integer totalLosses;
    private BigDecimal wlRatio;
    private Integer kills;
    private Integer deaths;
    private BigDecimal kdRatio;
    private BigDecimal edRatio;
    private Integer score;
    private BigDecimal avgScore;
    private BigDecimal averageAccuracy;
    private Integer timePlayed;
    private BigDecimal scorePerMinute;
    @Nullable
    private BigDecimal avgAttention;
    @Nullable
    private BigDecimal avgMeditation;

    public Double getTimePlayedHours() {
        return (double) (timePlayed / 3600d);
    }
}
