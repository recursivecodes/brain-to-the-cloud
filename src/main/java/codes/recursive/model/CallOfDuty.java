package codes.recursive.model;

import io.micronaut.core.annotation.Introspected;

import java.util.List;
import java.util.Map;

@Introspected
public class CallOfDuty {
    public static String MODERN_WARFARE = "mw";
    public static String COLD_WAR = "cw";
    public static String VANGUARD = "vg";
    public static String MODERN_WARFARE_II = "mw2";
    public static List<Map<String, String>> GAMES = List.of(
            Map.of("game", VANGUARD, "name", "Vanguard"),
            Map.of("game", MODERN_WARFARE_II, "name", "Modern Warfare II")
    );
    public static String XBL = "xbl";
}
