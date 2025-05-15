package kroryi.dagon.config;

public class NotificationFilterRequest {
    private Long uno;  // userId -> uno로 변경
    private String type;

    // Getters and Setters
    public Long getUno() {
        return uno;
    }

    public void setUno(Long uno) {
        this.uno = uno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
