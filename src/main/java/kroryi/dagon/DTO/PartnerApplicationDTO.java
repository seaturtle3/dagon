package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor// ✅ 기본 생성자 자동 생성
public class PartnerApplicationDTO {
    private Long pid;
    private Long uno;
    private String uid;
    private String pname;
    private String paddress;
    private String ceoName;
    private String pinfo;
    private String license;
    private String pstatus;
    private LocalDateTime paReviewedAt;
    private String paRejectionReason;
    private String uname;  // 신청자 이름
    private String displayName;
}
