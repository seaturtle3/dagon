package kroryi.dagon.enums;

import java.util.Arrays;
public enum ProdRegion {
    SEOUL("서울"),
    GYEONGGI("경기도"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEGU("대구"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GANGWON("강원도"),
    JEJU("제주도"),
    GYEONGBUK("경상북도"),
    GYEONGNAM("경상남도"),
    JEONBUK("전라북도"),
    JEONNAM("전라남도"),
    CHUNGBUK("충청북도"),
    CHUNGNAM("충청남도");

    private final String korean;

    ProdRegion(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

    public static ProdRegion fromKorean(String name) {
        return Arrays.stream(values())
                .filter(r -> r.getKorean().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown region: " + name));
    }
}
