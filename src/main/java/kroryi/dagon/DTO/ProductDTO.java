package kroryi.dagon.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private String prodName;
    private String prodRegion;
    private Integer maxPerson;
    private Integer minPerson;
    private String prodAddress;
    private String prodDescription;

}
