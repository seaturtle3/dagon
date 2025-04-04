package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link kroryi.dagon.entity.ProductImg}
 */
@Value
public class ProductImgDTO implements Serializable {
    Long id;
    Integer prodImgOrder;
    String prodImgUrl;
}