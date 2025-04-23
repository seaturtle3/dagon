package kroryi.dagon.enums;

import java.util.Arrays;
public enum ProdRegion {
    GANGWON("강원도"),
    GYEONGGI("경기도"),
    GYEONGNAM("경상남도"),
    GYEONGBUK("경상북도"),
    GWANGJU("광주"),
    DAEGU("대구"),
    DAEJEON("대전"),
    BUSAN("부산"),
    SEOUL("서울"),
    SEJONG("세종"),
    ULSAN("울산"),
    INCHEON("인천"),
    JEONNAM("전라남도"),
    JEONBUK("전라북도"),
    JEJU("제주도"),
    CHUNGNAM("충청남도"),
    CHUNGBUK("충청북도");

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

//package kroryi.dagon.enums;
//
//public enum ProdRegion {
//    SEOUL("서울"),
//    BUSAN("부산"),
//    DAEGU("대구"),
//    INCHEON("인천"),
//    GWANGJU("광주"),
//    DAEJEON("대전"),
//    ULSAN("울산"),
//    SEJONG("세종"),
//    GYEONGGI("경기도"),
//    GANGWON("강원도"),
//    CHUNGBUK("충청북도"),
//    CHUNGNAM("충청남도"),
//    JEONBUK("전라북도"),
//    JEONNAM("전라남도"),
//    GYEONGBUK("경상북도"),
//    GYEONGNAM("경상남도"),
//    JEJU("제주");
//
//    private final String korean;
//
//    ProdRegion(String koreanName) {
//        this.korean = koreanName;
//    }
//
//    public String getKorean() {
//        return korean;
//    }
//
//    @Override
//    public String toString() {
//        return korean;
//    }
//
//    public static ProdRegion fromKorean(String koreanName) {
//        for (ProdRegion region : values()) {
//            if (region.getKorean().equals(koreanName)) {
//                return region;
//            }
//        }
//        throw new IllegalArgumentException("존재하지 않는 지역명입니다: " + koreanName);
//    }
//}
