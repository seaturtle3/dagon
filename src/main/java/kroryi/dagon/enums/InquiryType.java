package kroryi.dagon.enums;

public enum InquiryType {
    PRODUCT("상품관련"),
    BUSINESS("제휴관련"),
    SYSTEM("시스템관련"),
    RESERVATION("예약관련"),
    CANCEL("예약취소관련");

    private final String korean;

    InquiryType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
