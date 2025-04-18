package kroryi.dagon.util.multtae;

public class LunarUtil {
    public static String getMulName(double lunAge) {
        int cycleDay = (int) Math.round(lunAge) % 15;

        if (cycleDay == 14) {
            return "조금";
        } else {
            return (cycleDay + 1) + "물"; // 0부터 시작이므로 +1
        }
    }
}
