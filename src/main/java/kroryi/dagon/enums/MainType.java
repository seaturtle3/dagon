package kroryi.dagon.enums;

public enum MainType {
    SEA("바다낚시"),
    FRESHWATER("민물낚시");

    private final String korean;

    MainType(String koreanName) {
        this.korean = koreanName;
    }

    public String getKorean() {
        return korean;
    }
}
