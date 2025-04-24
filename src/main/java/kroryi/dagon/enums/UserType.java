package kroryi.dagon.enums;

public enum UserType {
    USER("일반회원"),
    PARTNER("파트너"),
    ADMIN("관리자");

    private final String korean;

    UserType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}