package kroryi.dagon.DTO;

import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductDTO {
    private Long prodId;
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
    private LocalDate createdAt;
    private String prodThumbnail;


    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdId(product.getProdId());
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setMainType(product.getMainType());
        dto.setSubType(product.getSubType());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());

        // LocalDateTime -> LocalDate 변환
        if (product.getCreatedAt() != null) {
            dto.setCreatedAt(product.getCreatedAt().toLocalDate());  // LocalDateTime에서 LocalDate만 추출
        }

        dto.setProdThumbnail(product.getProdThumbnail());

        return dto;
    }
}
