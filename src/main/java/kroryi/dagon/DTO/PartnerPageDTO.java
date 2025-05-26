package kroryi.dagon.DTO;

import kroryi.dagon.entity.Partner;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartnerPageDTO {

    private Long uno;
    private String pname;
    private String pAddress;
    private String ceoName;
    private String pInfo;
    private String license;
    private String licenseImg;
    private String uname; // 유저 이름


    public PartnerPageDTO(Partner partner) {
        this.uno = partner.getUno();
        this.pname = partner.getPname();
        this.ceoName = partner.getCeoName();
        this.pAddress = partner.getPAddress();
        this.pInfo = partner.getPInfo();
        this.license = partner.getLicense();
        this.licenseImg = partner.getLicenseImg();
        this.uname = partner.getUser().getUname();  // User와 연관관계가 있다면
    }
}