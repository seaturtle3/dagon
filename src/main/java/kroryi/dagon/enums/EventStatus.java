package kroryi.dagon.enums;

public enum EventStatus {
    SCHEDULED("진행예정"),
    ONGOING("진행중"),
    COMPLETED("종료");

    private String korean;

    EventStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
