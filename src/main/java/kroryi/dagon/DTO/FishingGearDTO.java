package kroryi.dagon.DTO;

import kroryi.dagon.entity.ProductFishingGear;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ProductFishingGear}
 */
@Value
public class FishingGearDTO implements Serializable {
    Long id;
    String giconUrl;
    String gname;
}