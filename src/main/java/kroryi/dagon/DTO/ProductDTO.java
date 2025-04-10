package kroryi.dagon.DTO;

import kroryi.dagon.entity.Product;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Product}
 */
@Value
public class ProductDTO implements Serializable {
    Long id;
//    Product.ProdRegion prodRegion;
//    Product.MainType mainType;
//    Product.SubType subType;
    Integer maxPerson;
    Integer minPerson;
    BigDecimal weight;
    Instant createdAt;
    String pname;
    String prodAddress;
    String prodName;
    String uphone;
    String prodDescription;
    String prodEvent;
    String prodNotice;
}