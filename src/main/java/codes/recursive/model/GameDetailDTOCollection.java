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
public class GameDetailDTOCollection {
    private static String MIN = "min";
    private static String MAX = "max";

    private List<GameDetailDTO> gameDetailDTOList;
    private Map<String, Object> codLookups;

    private final Map<String, Function<GameDetailDTO, BigDecimal>> bigDecimalMethods = Map.ofEntries();
    private final Map<String, Function<GameDetailDTO, Integer>> integerMethods = Map.ofEntries();

    public String lookupMap(String game, String map) {
        if( map.equals("mp_jalo_oasis") ) return "Casablanca"; //not in the lookup table yet
        if( map.equals("mp_monsters") ) return "Mayhem"; //not in the lookup table yet
        if( map.equals("mp_gondola") ) return "Gondola"; //not in the lookup table yet
        if( map.equals("mp_shipmas_s4") ) return "Shipmas"; //not in the lookup table yet
        if( map.equals("mp_paradise") ) return "Paradise"; //not in the lookup table yet
        if( map.equals("mp_radar") ) return "Radar"; //not in the lookup table yet
        if( map.equals("mp_ar_alps") ) return "Alps"; //not in the lookup table yet
        return ((String) codLookups.get("maps:" + game + "-" + map + ":1")).replace("’", "'");
    }

    public String lookupMode(String game, String mode) {
        if( mode.equals("kspoint") ) return "Armageddon";
        if( mode.equals("control") ) return "Control";
        if( mode.equals("base") ) return "Arms Race";
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

    public BigDecimal getMinMaxBigDecimal(Function<GameDetailDTO, BigDecimal> method, String type, Boolean filterZero) {
        Optional<BigDecimal> ret = Optional.empty();
        Stream<BigDecimal> stream = gameDetailDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e.compareTo(new BigDecimal(0)) > 0);
        if(type.equals(MIN)) ret = stream.min(BigDecimal::compareTo);
        if(type.equals(MAX)) ret = stream.max(BigDecimal::compareTo);
        return ret.orElse(new BigDecimal(0));
    }

    public Integer getMinMaxInteger(Function<GameDetailDTO, Integer> method, String type, Boolean filterZero) {
        Optional<Integer> ret = Optional.empty();
        Stream<Integer> stream = gameDetailDTOList.stream().map(method);
        if(filterZero) stream = stream.filter(e -> e > 0);
        if(type.equals(MIN)) ret = stream.min(Integer::compare);
        if(type.equals(MAX)) ret = stream.max(Integer::compare);
        return ret.orElse(0);
    }
}
