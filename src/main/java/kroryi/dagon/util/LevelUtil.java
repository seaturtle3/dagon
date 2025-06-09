package kroryi.dagon.util;

import kroryi.dagon.enums.UserLevel;

public class LevelUtil {

    public static UserLevel calculateLevel(Integer points) {
        if (points >= 3000) return UserLevel.DIAMOND;
        else if (points >= 2000) return UserLevel.PLATINUM;
        else if (points >= 1000) return UserLevel.GOLD;
        else return UserLevel.SILVER;
    }

    public static int getNextLevelThreshold(UserLevel currentLevel) {
        return switch (currentLevel) {
            case SILVER -> 1000;
            case GOLD -> 2000;
            case PLATINUM -> 3000;
            case DIAMOND -> -1; // 최대 레벨
        };
    }
}
