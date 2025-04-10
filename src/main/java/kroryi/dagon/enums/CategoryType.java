package kroryi.dagon.enums;

public enum CategoryType {
    USERE("사용자"),
    PARTNER("파트너"),
    ADMIN("관리자");

    private final String korean;

    CategoryType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
