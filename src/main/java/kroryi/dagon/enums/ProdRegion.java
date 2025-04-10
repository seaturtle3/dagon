package kroryi.dagon.enums;

public enum ProdRegion {
    SEOUL("서울"),
    BUSAN("부산"),
    DEAGU("대구"),
    INCHEON("인천"),
    GWWANGJU("광주"),
    DEAGEON("대전"),
    ULSSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기도"),
    GANGWON("강원도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEJU("제주");

    private final String korean;

    ProdRegion(String koreanName) {
        this.korean = koreanName;
    }

    public String getKorean() {
        return korean;
    }

    @Override
    public String toString() {
        return korean;
    }
}
