package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@SuppressWarnings({"unused"})
public class RangeSummaryDTOCollection {
    private static String MIN = "min";
    private static String MAX = "max";

    private List<RangeSummaryDTO> rangeSummaryDTOList;

    public String getKdClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getKdRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getKdRatio, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getEdClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getEdRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getEdRatio, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getWlClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getWlRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getWlRatio, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getAttentionClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getAvgAttention, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getAvgAttention, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getMeditationClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getAvgMeditation, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getAvgMeditation, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getScorePerMinute, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(RangeSummaryDTO::getScorePerMinute, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public BigDecimal getMinMaxBigDecimal(Function<RangeSummaryDTO, BigDecimal> method, String type, Boolean filterZero) {
        Optional<BigDecimal> ret = Optional.empty();
        Stream<BigDecimal> stream = rangeSummaryDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e.compareTo(new BigDecimal(0)) > 0);
        if(type.equals(MIN)) ret = stream.min(BigDecimal::compareTo);
        if(type.equals(MAX)) ret = stream.max(BigDecimal::compareTo);
        return ret.orElse(new BigDecimal(0));
    }

    public Integer getMinMaxInteger(Function<RangeSummaryDTO, Integer> method, String type, Boolean filterZero) {
        Optional<Integer> ret = Optional.empty();
        Stream<Integer> stream = rangeSummaryDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e > 0);
        if(type.equals(MIN)) ret = stream.min(Integer::compare);
        if(type.equals(MAX)) ret = stream.max(Integer::compare);
        return ret.orElse(0);
    }

}
