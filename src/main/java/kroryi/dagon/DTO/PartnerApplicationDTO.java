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
    private Long id;
    private Long uno;
    private String pname;
    private String paddress;
    private String pceo;
    private String pinfo;
    private String plicense;
    private String paStatus;
    private LocalDateTime paCreatedAt;
    private LocalDateTime paReviewedAt;
    private String paRejectionReason;
    private String uname;  // 신청자 이름

}
