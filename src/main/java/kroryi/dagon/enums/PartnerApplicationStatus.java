package kroryi.dagon.enums;

public enum PartnerApplicationStatus {
    PENDING("심사중"),
    APPROVED("승인됨"),
    REJECTED("반려");

    private final String korean;

    PartnerApplicationStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
