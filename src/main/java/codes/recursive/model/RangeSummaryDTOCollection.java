package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@SuppressWarnings({"unused"})
public class RangeSummaryDTOCollection {
    private List<RangeSummaryDTO> rangeSummaryDTOList;

    public String getKdClass(BigDecimal val) {
        if(val.compareTo(getMinKd()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxKd()) == 0) return "bg-success text-white";
        return "";
    }

    public String getEdClass(BigDecimal val) {
        if(val.compareTo(getMinEd()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxEd()) == 0) return "bg-success text-white";
        return "";
    }

    public String getWlClass(BigDecimal val) {
        if(val.compareTo(getMinWl()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxWl()) == 0) return "bg-success text-white";
        return "";
    }

    public String getAttentionClass(BigDecimal val) {
        if(val.compareTo(getMinAttention()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxAttention()) == 0) return "bg-success text-white";
        return "";
    }

    public String getMeditationClass(BigDecimal val) {
        if(val.compareTo(getMinMeditation()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxMeditation()) == 0) return "bg-success text-white";
        return "";
    }

    public String getScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinScorePerMinute()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxScorePerMinute()) == 0) return "bg-success text-white";
        return "";
    }

    public BigDecimal getMaxKd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getKdRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinKd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getKdRatio)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxEd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getEdRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinEd() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getEdRatio)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxWl() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getWlRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinWl() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getWlRatio)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxAttention() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgAttention)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinAttention() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgAttention)
                .filter(Objects::nonNull)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxMeditation() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgMeditation)
                .filter(Objects::nonNull)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinMeditation() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getAvgMeditation)
                .filter(Objects::nonNull)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxScorePerMinute() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getScorePerMinute)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinScorePerMinute() {
        return rangeSummaryDTOList
                .stream()
                .map(RangeSummaryDTO::getScorePerMinute)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }
}
