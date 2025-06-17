package kroryi.dagon.DTO;

import kroryi.dagon.entity.Report;
import kroryi.dagon.enums.TargetType;
import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private Long reportedUserId;
    private String reportedUserUid;
    private String reportedUserUname;
    private Boolean reportedUserActive; // 👈 추가: 신고당한 유저의 활성화 여부
    private String reporterUname;
    private TargetType targetType;  // 신고 대상 타입
    private Long targetId;  // 신고 대상 ID
    private String reason;
    private String createdAt;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.reportedUserId = report.getReported().getUno();
        this.reportedUserUid = report.getReported().getUid();
        this.reportedUserUname = report.getReported().getUname();
        this.reportedUserActive = report.getReported().isActive();  // 👈 여기가 핵심
        this.reporterUname = report.getReporter() != null ? report.getReporter().getUname() : null;
        this.targetType = report.getTargetType();
        this.targetId = report.getTargetId();
        this.reason = report.getReason();
        this.createdAt = report.getCreatedAt() != null ? report.getCreatedAt().toString() : null;
    }
}
