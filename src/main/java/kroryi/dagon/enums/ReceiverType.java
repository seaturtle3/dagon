package kroryi.dagon.enums;

public enum ReceiverType {
    PARTNER("파트너"),
    ADMIN("관리자");

    private final String korean;

    ReceiverType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}