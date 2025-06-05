package kroryi.dagon.DTO;

import kroryi.dagon.entity.Partner;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.Partner}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO implements Serializable {
    Long uno;
    String pname;
    String pAddress;
    String ceoName;
    String pInfo;
    String license;
    String licenseImg;



    public PartnerDTO (Partner partner) {
        this.uno = partner.getUno();
        this.pname = partner.getPname();
        this.ceoName = partner.getCeoName();
        this.pAddress = partner.getPAddress();
        this.pInfo = partner.getPInfo();
        this.license = partner.getLicense();
        this.licenseImg = partner.getLicenseImg();
    }


}