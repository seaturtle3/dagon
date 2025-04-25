package kroryi.dagon.DTO;

import kroryi.dagon.entity.Report;
import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private Long reportedUserId;
    private String reportedUserUid;
    private String reportedUserUname;
    private String reporterUname;     // 신고자 이름 추가
    private String reason;
    private String createdAt;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.reportedUserId = report.getReported().getUno();
        this.reportedUserUid = report.getReported().getUid();
        this.reportedUserUname = report.getReported().getUname();
        this.reporterUname = report.getReporter() != null ? report.getReporter().getUname() : null;
        this.reason = report.getReason();
        this.createdAt = report.getCreatedAt() != null ? report.getCreatedAt().toString() : null;
    }
}
