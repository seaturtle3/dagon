package kroryi.dagon.enums;

public enum UserLevel {
    SILVER("실버"),
    GOLD("골드"),
    PLATINUM("플래티넘"),
    DIAMOND("다이아몬드");

    private final String korean;

    UserLevel(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
