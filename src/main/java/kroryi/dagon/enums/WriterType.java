package kroryi.dagon.enums;

public enum WriterType {
    ADMIN("관리자"),
    PARTNER("협력사"),
    USER("사용자");

    private final String korean;

    WriterType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
