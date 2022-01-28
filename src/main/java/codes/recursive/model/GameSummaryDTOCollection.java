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

    public String getWlClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getWlRatio, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getWlRatio, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getAvgKillsClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgKills, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgKills, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getAvgDeathsClass(BigDecimal val) {
        if(val.compareTo( getMinMaxBigDecimal(GameSummaryDTO::getAvgDeaths, MIN, false) ) == 0) return "bg-success text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgDeaths, MAX, false)) == 0) return "bg-danger text-white";
        return "";
    }

    public String getKdClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalKdRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalKdRatio, MAX, true)) == 0) return "bg-success text-white";
        return "";
    }

    public String getEdClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalEdRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalKdRatio, MAX, true)) == 0) return "bg-success text-white";
        return "";
    }

    public String getLongestStreakClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getLongestStreak, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getTotalScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalScorePerMinute, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalScorePerMinute, MAX, true)) == 0) return "bg-success text-white";
        return "";
    }

    public String getAvgScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgScorePerMinute, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgScorePerMinute, MAX, true)) == 0) return "bg-success text-white";
        return "";
    }

    public String getTotalAccuracyClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalAccuracy, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getTotalAccuracy, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getAvgAccuracyClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAverageAccuracy, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAverageAccuracy, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getPctTimeMovingClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgPctTimeMoving, MIN, true)) == 0) return "bg-success text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getAvgPctTimeMoving, MAX, true)) == 0) return "bg-danger text-white";
        return "";
    }

    public String getMaxKillsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxKills, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxKills, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getMaxEliminationsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxEliminations, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxEliminations, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getMinKillsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinKills, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinKills, MAX, true)) == 0) return "bg-success text-white";
        return "";
    }

    public String getMaxDeathsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxDeaths, MIN, false)) == 0) return "bg-success text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxDeaths, MAX, false)) == 0) return "bg-danger text-white";
        return "";
    }

    public String getMinDeathsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinDeaths, MIN, true)) == 0) return "bg-success text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinDeaths, MAX, true)) == 0) return "bg-danger text-white";
        return "";
    }

    public String getMaxAssistsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxAssists, MIN, false)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMaxAssists, MAX, false)) == 0) return "bg-success text-white";
        return "";
    }

    public String getMinAssistsClass(Integer val) {
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinAssists, MIN, true)) == 0) return "bg-success text-white";
        if(val.compareTo(getMinMaxInteger(GameSummaryDTO::getMinAssists, MAX, true)) == 0) return "bg-danger text-white";
        return "";
    }

    public String getMaxKdRatioClass(BigDecimal val) {
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getMaxKdRatio, MIN, true)) == 0) return "bg-danger text-white";
        if(val.compareTo(getMinMaxBigDecimal(GameSummaryDTO::getMaxKdRatio, MAX, true)) == 0) return "bg-success text-white";
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
