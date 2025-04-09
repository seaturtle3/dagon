package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Partner}
 */
@Value
public class PartnerDTO implements Serializable {
    Long uno;
    String pname;
    String pAddress;
    String ceoName;
    String pInfo;
    String license;
    String licenseImg;
}