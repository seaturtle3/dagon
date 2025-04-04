package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link kroryi.dagon.entity.PartnerApplication}
 */
@Value
public class PartnerApplicationDTO implements Serializable {
    Long id;
    String pname;
    String paddress;
    String pceo;
    String pinfo;
    String plicense;
    String paStatus;
    LocalDateTime paCreatedAt;
    LocalDateTime paReviewedAt;
    String paRejectionReason;
}