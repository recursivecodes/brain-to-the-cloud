package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Introspected
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Builder
@SuppressWarnings({"unused"})
public class GameSummaryDTOCollection {
    private List<GameSummaryDTO> gameSummaryDTOList;
    private Map<String, Object> codLookups;

    public String lookupMap(String game, String map) {
        if( map.equals("mp_shipmas_s4") ) return "Shipmas"; //not in the lookup table yet
        if( map.equals("mp_paradise") ) return "Paradise"; //not in the lookup table yet
        if( map.equals("mp_radar") ) return "Radar"; //not in the lookup table yet
        return ((String) codLookups.get("maps:" + game + "-" + map + ":1")).replace("’", "'");
    }

    public String getWlClass(BigDecimal val) {
        if(val.compareTo(getMinWl()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxWl()) == 0) return "bg-success text-white";
        return "";
    }

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

    public String getLongestStreakClass(Integer val) {
        if(val.compareTo(getMaxLongestStreak()) == 0) return "bg-success text-white";
        return "";
    }

    public String getTotalScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinTotalScorePerMinute()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxTotalScorePerMinute()) == 0) return "bg-success text-white";
        return "";
    }

    public String getAvgScorePerMinuteClass(BigDecimal val) {
        if(val.compareTo(getMinAvgScorePerMinute()) == 0) return "bg-danger text-white";
        if(val.compareTo(getMaxAvgScorePerMinute()) == 0) return "bg-success text-white";
        return "";
    }

    public String getTotalAccuracyClass(BigDecimal val) {
        if(val.compareTo(getMinTotalAccuracy()) == 0) return "bg-success text-white";
        if(val.compareTo(getMaxTotalAccuracy()) == 0) return "bg-danger text-white";
        return "";
    }

    public String getAvgAccuracyClass(BigDecimal val) {
        if(val.compareTo(getMinAvgAccuracy()) == 0) return "bg-success text-white";
        if(val.compareTo(getMaxAvgAccuracy()) == 0) return "bg-danger text-white";
        return "";
    }

    public BigDecimal getMaxWl() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getWlRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinWl() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getWlRatio)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public Integer getMaxLongestStreak() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getLongestStreak)
                .max(Integer::compare)
                .orElse(0);
    }

    public BigDecimal getMaxKd() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalKdRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinKd() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalKdRatio)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxEd() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalEdRatio)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinEd() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalEdRatio)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxTotalScorePerMinute() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalScorePerMinute)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinTotalScorePerMinute() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalScorePerMinute)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxAvgScorePerMinute() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getAvgScorePerMinute)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinAvgScorePerMinute() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getAvgScorePerMinute)
                .filter(e -> e.compareTo(new BigDecimal(0)) > 0)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinTotalAccuracy() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalAccuracy)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxTotalAccuracy() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getTotalAccuracy)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMinAvgAccuracy() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getAverageAccuracy)
                .max(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }

    public BigDecimal getMaxAvgAccuracy() {
        return gameSummaryDTOList
                .stream()
                .map(GameSummaryDTO::getAverageAccuracy)
                .min(BigDecimal::compareTo)
                .orElse(new BigDecimal(0));
    }
}
