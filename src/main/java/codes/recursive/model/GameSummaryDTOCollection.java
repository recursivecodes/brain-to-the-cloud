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
public class GameSummaryDTOCollection {
    private static String MIN = "min";
    private static String MAX = "max";

    private List<GameSummaryDTO> gameSummaryDTOList;
    private Map<String, Object> codLookups;
    private final Map<String, Function<GameSummaryDTO, BigDecimal>> bigDecimalMethods = Map.ofEntries(
            Map.entry("getAvgKills", GameSummaryDTO::getAvgKills),
            Map.entry("getAvgDeaths", GameSummaryDTO::getAvgDeaths),
            Map.entry("getTotalKdRatio", GameSummaryDTO::getTotalKdRatio),
            Map.entry("getTotalEdRatio", GameSummaryDTO::getTotalEdRatio),
            Map.entry("getTotalScorePerMinute", GameSummaryDTO::getTotalScorePerMinute),
            Map.entry("getAvgScorePerMinute", GameSummaryDTO::getAvgScorePerMinute),
            Map.entry("getTotalAccuracy", GameSummaryDTO::getTotalAccuracy),
            Map.entry("getAverageAccuracy", GameSummaryDTO::getAverageAccuracy),
            Map.entry("getAvgPctTimeMoving", GameSummaryDTO::getAvgPctTimeMoving),
            Map.entry("getWlRatio", GameSummaryDTO::getWlRatio),
            Map.entry("getMaxKdRatio", GameSummaryDTO::getMaxKdRatio)
    );
    private final Map<String, Function<GameSummaryDTO, Integer>> integerMethods = Map.ofEntries(
            Map.entry("getLongestStreak", GameSummaryDTO::getLongestStreak),
            Map.entry("getMaxKills", GameSummaryDTO::getMaxKills),
            Map.entry("getMinKills", GameSummaryDTO::getMinKills),
            Map.entry("getMaxEliminations", GameSummaryDTO::getMaxEliminations),
            Map.entry("getMaxDeaths", GameSummaryDTO::getMaxDeaths),
            Map.entry("getMinDeaths", GameSummaryDTO::getMinDeaths),
            Map.entry("getMaxAssists", GameSummaryDTO::getMaxAssists),
            Map.entry("getMinAssists", GameSummaryDTO::getMinAssists)
    );

    public String lookupMap(String game, String map) {
        if( map.equals("mp_shipmas_s4") ) return "Shipmas"; //not in the lookup table yet
        if( map.equals("mp_paradise") ) return "Paradise"; //not in the lookup table yet
        if( map.equals("mp_radar") ) return "Radar"; //not in the lookup table yet
        return ((String) codLookups.get("maps:" + game + "-" + map + ":1")).replace("’", "'");
    }

    public String lookupMode(String game, String mode) {
        if( mode.equals("kspoint") ) return "Armageddon";
        return ((String) codLookups.get("game-modes:" + game + "-" + mode + ":1")).replace("’", "'");
    }

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

    public BigDecimal getMinMaxBigDecimal(Function<GameSummaryDTO, BigDecimal> method, String type, Boolean filterZero) {
        Optional<BigDecimal> ret = Optional.empty();
        Stream<BigDecimal> stream = gameSummaryDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e.compareTo(new BigDecimal(0)) > 0);
        if(type.equals(MIN)) ret = stream.min(BigDecimal::compareTo);
        if(type.equals(MAX)) ret = stream.max(BigDecimal::compareTo);
        return ret.orElse(new BigDecimal(0));
    }

    public Integer getMinMaxInteger(Function<GameSummaryDTO, Integer> method, String type, Boolean filterZero) {
        Optional<Integer> ret = Optional.empty();
        Stream<Integer> stream = gameSummaryDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e > 0);
        if(type.equals(MIN)) ret = stream.min(Integer::compare);
        if(type.equals(MAX)) ret = stream.max(Integer::compare);
        return ret.orElse(0);
    }
}
