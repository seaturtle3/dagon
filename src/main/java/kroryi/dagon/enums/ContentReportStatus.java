package kroryi.dagon.enums;

public enum ContentReportStatus {
    PENDING("처리 대기"),
    APPROVED("승인됨"),
    REJECTED("신고 취소");

    private final String korean;

    ContentReportStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
