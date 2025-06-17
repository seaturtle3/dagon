package kroryi.dagon.DTO;

import kroryi.dagon.enums.TargetType;
import lombok.Data;

@Data
public class ReportRequestDTO {
    private String reportedUid;  // 신고당한 유저의 UID
    private TargetType targetType;  // 신고 대상 타입
    private Long targetId;  // 신고 대상 ID (게시글, 댓글, 상품 ID)
    private String reason;
}
