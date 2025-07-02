package kroryi.dagon.enums;

public enum ReservationStatus {
    PENDING("대기"),
    PAID("결제완료"),
    CANCELED("취소됨"),
    COMPLETED("이용완료");

    private final String korean;

    ReservationStatus(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
