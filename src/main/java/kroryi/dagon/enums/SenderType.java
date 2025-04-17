package kroryi.dagon.enums;

public enum SenderType {
    SYSTEM, ADMIN, PARTNER;

    // 대소문자 구분 없이 String 값을 SenderType으로 변환하는 메서드
    public static SenderType fromString(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Sender type cannot be null");
        }

        // 문자열을 대소문자 구분 없이 비교하기 위해 toUpperCase() 사용
        String upperCaseType = type.toUpperCase();

        return switch (upperCaseType) {
            case "SYSTEM" -> SYSTEM;
            case "ADMIN" -> ADMIN;
            case "PARTNER" -> PARTNER;
            default -> throw new IllegalArgumentException("Unknown sender type: " + type);
        };
    }
}