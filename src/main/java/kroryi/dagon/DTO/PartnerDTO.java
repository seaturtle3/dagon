package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Partner}
 */
@Value
public class PartnerDTO implements Serializable {
    Long id;
    String pname;
    String paddress;
    String pceo;
    String pinfo;
    String plicense;
    String plicenseImg;
    Instant pcreatedAt;
}