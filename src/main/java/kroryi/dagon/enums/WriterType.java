package kroryi.dagon.enums;

public enum WriterType {
    PARTNER("파트너"),
    USER("일반회원");

    private final String korean;

    WriterType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }
}
