package kroryi.dagon.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductOptionDTO {
    Long optId;
    String optName;
    String optTime;
    String optDescription;
    BigDecimal price;
    Long prodId;
    String prodName;
}
