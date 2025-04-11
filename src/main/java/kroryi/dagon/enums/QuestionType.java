package kroryi.dagon.enums;

public enum QuestionType {
    PRODUCT("상품문의"),
    RESERVATION("예약문의"),
    CANCELLATION("예약취소문의"),
    SYSTEM("시스템문의"),
    BUSINESS("제휴문의");

    private final String korean;

    QuestionType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
