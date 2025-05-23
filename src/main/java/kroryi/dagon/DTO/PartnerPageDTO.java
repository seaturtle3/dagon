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




    public static PartnerPageDTO fromEntity(Partner partner) {
        PartnerPageDTO dto = new PartnerPageDTO();
        dto.setUno(partner.getUno());
        dto.setPname(partner.getPname());
        dto.setPAddress(partner.getPAddress());
        dto.setCeoName(partner.getCeoName());
        dto.setPInfo(partner.getPInfo());
        dto.setLicense(partner.getLicense());
        dto.setUname(partner.getUser().getUname());
        return dto;
    }
}