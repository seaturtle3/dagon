package kroryi.dagon.DTO;

import kroryi.dagon.enums.SenderType;
import lombok.Builder;
import lombok.Data;

@Data

public class NotificationDTO {
    private Long id;
    private Long receiverId;
    private Long senderId;
    private Long reservationId; // 예약 정보 ID
    private SenderType senderType;
    private String type;
    private String title;
    private String content;
    private boolean isRead;            // 읽음 여부
    private String createdAt;
    private String senderName;     // 발신자 이름


}
