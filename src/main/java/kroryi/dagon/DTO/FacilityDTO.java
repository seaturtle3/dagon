package kroryi.dagon.DTO;

import kroryi.dagon.entity.ProductFacility;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link ProductFacility}
 */
@Value
public class ProductFacilityDTO implements Serializable {
    Long id;
    String faname;
    String faiconUrl;
}