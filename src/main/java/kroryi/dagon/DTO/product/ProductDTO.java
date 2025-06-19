package kroryi.dagon.DTO.product;

import kroryi.dagon.entity.product.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.stream;

@Data
public class ProductDTO {
    private Long prodId;
    private String prodName;
    private ProdRegion prodRegion;
    private String prodRegionKorean;
    private MainType mainType;
    private String mainTypeKorean;
    private SubType subType;
    private String subTypeKorean;
    private Integer maxPerson;
    private Integer minPerson;
    private BigDecimal weight;
    private String prodAddress;
    private String prodDescription;
    private String prodEvent;
    private String prodNotice;
    private LocalDate createdAt;
    private String prodThumbnail;
    private boolean deleted;
    private List<String> fishSpeciesNames;


    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdId(product.getProdId());
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setProdRegionKorean(product.getProdRegion().getKorean());
        dto.setMainType(product.getMainType());
        dto.setMainTypeKorean(product.getMainType().getKorean());
        dto.setSubType(product.getSubType());
        dto.setSubTypeKorean(product.getSubType().getKorean());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        dto.setDeleted(product.isDeleted());

        // LocalDateTime -> LocalDate 변환
        if (product.getCreatedAt() != null) {
            dto.setCreatedAt(product.getCreatedAt().toLocalDate());  // LocalDateTime에서 LocalDate만 추출
        }

        dto.setProdThumbnail(product.getProdThumbnail());

        return dto;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setProdName(this.getProdName());
        product.setProdRegion(this.getProdRegion());
        product.setMainType(this.getMainType());
        product.setSubType(this.getSubType());
        product.setMaxPerson(this.getMaxPerson());
        product.setMinPerson(this.getMinPerson());
        product.setWeight(this.getWeight());
        product.setProdAddress(this.getProdAddress());
        product.setProdDescription(this.getProdDescription());
        product.setProdEvent(this.getProdEvent());
        product.setProdNotice(this.getProdNotice());
        product.setProdThumbnail(this.getProdThumbnail());
        return product;
    }

}
