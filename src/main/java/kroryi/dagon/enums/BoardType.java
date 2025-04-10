package kroryi.dagon.enums;

public enum BoardType {
    DIARY("조행기"),
    REPORT("조황정보"),
    FREE("자유게시판"),
    PRODUCT("상품");

    private final String korean;

    BoardType(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

}
