package kroryi.dagon.DTO;

import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    public Long prodId;
    public String prodName;
    public ProdRegion prodRegion;
    public MainType mainType;
    public SubType subType;
    public Integer maxPerson;
    public Integer minPerson;
    public BigDecimal weight;
    public String prodAddress;
    public String prodDescription;
    public String prodEvent;
    public String prodNotice;


}
