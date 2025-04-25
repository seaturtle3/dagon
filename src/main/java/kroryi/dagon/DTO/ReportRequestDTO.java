package kroryi.dagon.DTO;

import lombok.Data;

@Data
public class ReportRequestDTO {
    private String reportedUid;  // 신고당한 유저의 UID
    private String reason;

}
