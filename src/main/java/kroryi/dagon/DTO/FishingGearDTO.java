package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.FishingGear}
 */
@Value
public class FishingGearDTO implements Serializable {
    Long id;
    String giconUrl;
    String gname;
}