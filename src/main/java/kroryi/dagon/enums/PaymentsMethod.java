package kroryi.dagon.enums;

public enum PaymentsMethod {
    BANK_TRANSFER("무통장 입금"),
    CARD("신용카드"),
    KAKAO_PAY("카카오페이");

    private final String korean;

    PaymentsMethod(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
