package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@SuppressWarnings({"unused"})
public class RangeSummaryDTOCollection {
    private List<RangeSummaryDTO> rangeSummaryDTOList;

    public Double getMaxKd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getKdRatio)
                .max(Double::compare)
                .orElse(0D);
    }

    public Double getMaxEd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getEdRatio)
                .max(Double::compare)
                .orElse(0D);
    }

    public Double getMinKd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getKdRatio)
                .filter(e -> e > 0)
                .min(Double::compare)
                .orElse(0D);
    }

    public Double getMinEd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getEdRatio)
                .filter(e -> e > 0)
                .min(Double::compare)
                .orElse(0D);
    }

    public Double getMinAttention() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgAttention)
                .filter(Objects::nonNull)
                .filter(e -> e > 0)
                .min(Double::compare)
                .orElse(0D);
    }

    public Double getMinMeditation() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgMeditation)
                .filter(Objects::nonNull)
                .filter(e -> e > 0)
                .min(Double::compare)
                .orElse(0D);
    }


    public Double getMaxAttention() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgAttention)
                .filter(Objects::nonNull)
                .max(Double::compare)
                .orElse(0D);
    }

    public Double getMaxMeditation() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgMeditation)
                .filter(Objects::nonNull)
                .max(Double::compare)
                .orElse(0D);
    }

    public Double getMaxScorePerMinute() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getScorePerMinute)
                .max(Double::compare)
                .orElse(0D);
    }

    public Double getMinScorePerMinute() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getScorePerMinute)
                .filter(e -> e > 0)
                .min(Double::compare)
                .orElse(0D);
    }
}
