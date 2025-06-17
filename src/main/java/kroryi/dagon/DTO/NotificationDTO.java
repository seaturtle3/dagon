package kroryi.dagon.DTO;

import kroryi.dagon.enums.SenderType;
import lombok.Builder;
import lombok.Data;

@Data

public class NotificationDTO {
    private Long id;
    private String  receiverUid;
    private String  senderUid;
    private Long reservationId; // 예약 정보 ID
    private SenderType senderType;
    private String type;
    private String title;
    private String content;
    private String senderName;         // ex: 관리자 이름 or 시스템
    private boolean isRead;            // 읽음 여부
    private String createdAt;
}
