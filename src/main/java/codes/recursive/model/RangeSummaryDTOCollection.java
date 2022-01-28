package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@SuppressWarnings({"unused", "Duplicates"})
public class RangeSummaryDTOCollection {
    private static String MIN = "min";
    private static String MAX = "max";
    private final Map<String, Function<RangeSummaryDTO, BigDecimal>> bigDecimalMethods = Map.ofEntries(
            Map.entry("getKdRatio", RangeSummaryDTO::getKdRatio),
            Map.entry("getEdRatio", RangeSummaryDTO::getEdRatio),
            Map.entry("getWlRatio", RangeSummaryDTO::getWlRatio),
            Map.entry("getAvgAttention", RangeSummaryDTO::getAvgAttention),
            Map.entry("getAvgMeditation", RangeSummaryDTO::getAvgMeditation),
            Map.entry("getScorePerMinute", RangeSummaryDTO::getScorePerMinute)
    );

    private final Map<String, Function<RangeSummaryDTO, Integer>> integerMethods = Map.ofEntries();
    private List<RangeSummaryDTO> rangeSummaryDTOList;

    public String getBigDecimalClass(BigDecimal val, String methodName, Boolean filterZero, Boolean inverse) {
        if(val.compareTo(getMinMaxBigDecimal(bigDecimalMethods.get(methodName), MIN, filterZero)) == 0) return inverse ? "bg-success text-white" : "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(bigDecimalMethods.get(methodName), MAX, filterZero)) == 0) return inverse ? "bg-danger text-white" : "bg-success text-white";
        return "";
    }

    public String getIntegerClass(Integer val, String methodName, Boolean filterZero, Boolean inverse) {
        if(val.compareTo(getMinMaxInteger(integerMethods.get(methodName), MIN, filterZero)) == 0) return inverse ? "bg-success text-white" : "bg-danger text-white";
        if(val.compareTo(getMinMaxInteger(integerMethods.get(methodName), MAX, filterZero)) == 0) return inverse ? "bg-danger text-white" : "bg-success text-white";
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
