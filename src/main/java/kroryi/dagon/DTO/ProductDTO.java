package kroryi.dagon.DTO;

import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String prodName;
    private ProdRegion prodRegion;
    private MainType mainType;
    private SubType subType;
    private Integer maxPerson;
    private Integer minPerson;
    private BigDecimal weight;
    private String prodAddress;
    private String prodDescription;
    private String prodEvent;
    private String prodNotice;


}
