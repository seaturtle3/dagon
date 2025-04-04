package kroryi.dagon.DTO;

import kroryi.dagon.entity.Facility;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Facility}
 */
@Value
public class FacilityDTO implements Serializable {
    Long id;
    String faname;
    String faiconUrl;
}