package kroryi.dagon.enums;

public enum ApplicationStatus {
    PENDING("심사중"),
    APPROVED("승인됨"),
    REJECTED("반려");

    private final String korean;

    ApplicationStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    public static ApplicationStatus from(String input) {
        for (ApplicationStatus status : values()) {
            if (status.name().equalsIgnoreCase(input) || status.korean.equals(input)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 상태값입니다: " + input);
    }
}