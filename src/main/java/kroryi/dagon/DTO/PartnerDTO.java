package kroryi.dagon.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.Partner}
 */

@Getter
@Setter
public class PartnerDTO implements Serializable {
    Long uno;
    String pname;
    String pAddress;
    String ceoName;
    String pInfo;
    String license;
    String licenseImg;


}