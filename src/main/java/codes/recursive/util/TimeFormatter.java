package codes.recursive.util;

import java.util.Arrays;
import java.util.stream.Collectors;
public class TimeFormatter {
    public static String formatDuration(int seconds) {
        return seconds == 0 ? "now" :
                Arrays.stream(
                                new String[]{
                                        formatTime("year",  (seconds / 31536000)),
                                        formatTime("day",   (seconds / 86400)%365),
                                        formatTime("hour",  (seconds / 3600)%24),
                                        formatTime("minute",(seconds / 60)%60),
                                        formatTime("second",(seconds%3600)%60)})
                        .filter(e->e!="")
                        .collect(Collectors.joining(", "))
                        .replaceAll(", (?!.+,)", " and ");
    }
    public static String formatTimeDurationShort(int seconds) {
        return seconds == 0 ? "now" :
                Arrays.stream(
                                new String[]{
                                        formatTimeShort("",(seconds / 60)%60, false),
                                        formatTimeShort("",(seconds%3600)%60, true)})
                        .filter(e->e!="")
                        .collect(Collectors.joining(", "))
                        .replaceAll(", (?!.+,)", ":");
    }
    public static String formatTimeShort(String s, int time, Boolean zeroPad){
        return time==0 ? "" : (zeroPad ? String.format("%02d", time) : time) + s;
    }
    public static String formatTime(String s, int time){
        return time==0 ? "" : time + " " + s + (time==1?"" : "s");
    }
}