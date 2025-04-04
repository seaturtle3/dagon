package kroryi.dagon.DTO;

import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ToString
@Data
@NoArgsConstructor  // ✅ 기본 생성자 자동 생성
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

    public PartnerApplicationDTO(Long id, Long uno, String pname, String p_address, String pceo, String pinfo, String plicense,
                                 String paStatus, LocalDateTime paCreatedAt, LocalDateTime paReviewedAt,
                                 String paRejectionReason, String uname) {
        this.id = id;
        this.uno = uno;
        this.pname = pname;
        this.paddress = p_address;
        this.pceo = pceo;
        this.pinfo = pinfo;
        this.plicense = plicense;
        this.paStatus = paStatus;
        this.paCreatedAt = paCreatedAt;
        this.paReviewedAt = paReviewedAt;
        this.paRejectionReason = paRejectionReason;
        this.uname = uname;
    }
}