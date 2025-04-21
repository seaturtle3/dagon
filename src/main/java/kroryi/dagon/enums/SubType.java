package kroryi.dagon.enums;

public enum SubType {
//    // 바다낚시
//    BREAK_WATER("방파제", MainType.SEA),
//    ROCKY_SHORE("갯바위", MainType.SEA),
//    ESTUARY("하구", MainType.SEA),
//    BOAT("선상", MainType.SEA),
//    MUD_FLAT("갯벌", MainType.SEA),
//    ARTIFICIAL("인공낚시터", MainType.SEA),
//    OPEN_SEA("해상", MainType.SEA),
//    BEACH("해변", MainType.SEA),
//    REEF("암초", MainType.SEA),
//    OTHER_SEA("기타_바다", MainType.SEA),
//
//    // 민물낚시
//    RIVER("강", MainType.FRESHWATER),
//    RESERVOIR("저수지", MainType.FRESHWATER),
//    DAM("댐", MainType.FRESHWATER),
//    POND("연못", MainType.FRESHWATER),
//    SMALL_LAKE("소류지", MainType.FRESHWATER),
//    CANAL("수로", MainType.FRESHWATER),
//    FLOATING_PLATFORM("좌대", MainType.FRESHWATER),
//    OPEN_AREA("노지", MainType.FRESHWATER),
//    OTHER_FRESHWATER("기타_민물", MainType.FRESHWATER);

    // 바다낚시
    BREAK_WATER("방파제"),
    ROCKY_SHORE("갯바위"),
    ESTUARY("하구"),
    BOAT("선상"),
    MUD_FLAT("갯벌"),
    ARTIFICIAL("인공낚시터"),
    OPEN_SEA("해상"),
    BEACH("해변"),
    REEF("암초"),
    OTHER_SEA("기타_바다"),

    // 민물낚시
    RIVER("강"),
    RESERVOIR("저수지"),
    DAM("댐"),
    POND("연못"),
    SMALL_LAKE("소류지"),
    CANAL("수로"),
    FLOATING_PLATFORM("좌대"),
    OPEN_AREA("노지"),
    OTHER_FRESHWATER("기타_민물");

    private final String korean;
//    private final MainType mainType;

//    SubType(String korean, MainType mainType) {
//        this.korean = korean;
//        this.mainType = mainType;
//    }
    SubType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

//    public MainType getMainType() {
//        return mainType;
//    }
}
