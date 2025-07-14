package kroryi.dagon.DTO.product;

import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.product.ProductImage;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long uno;
    private Partner partner;

    // 상품 기본 가격
    private BigDecimal prodPrice;

    private boolean deleted;
    private List<String> fishSpeciesNames;
    private List<ProductOptionDTO> options;

    private List<String> prodImageNames; // 썸네일 여러 개
    private List<String> deleteImageNames; // 삭제할 이미지 경로
    private List<byte[]> prodImageDataList; // 이미지 바이너리 리스트

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
        dto.setProdPrice(product.getProdPrice());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        dto.setDeleted(product.isDeleted());
        if (product.getOptions() != null) {
            List<ProductOptionDTO> optionDTOs = product.getOptions().stream().map(option -> {
                ProductOptionDTO dtoOpt = new ProductOptionDTO();
                dtoOpt.setOptId(option.getOptId());
                dtoOpt.setOptName(option.getOptName());
                dtoOpt.setOptTime(option.getOptTime());
                dtoOpt.setOptDescription(option.getOptDescription());
                dtoOpt.setPrice(option.getPrice());
                dtoOpt.setProdId(product.getProdId());
                dtoOpt.setProdName(product.getProdName());
                return dtoOpt;
            }).toList();
            dto.setOptions(optionDTOs);
        }
        dto.setUno(product.getPartner().getUno());
        // 파트너 정보는 필요한 필드만 설정하여 무한 재귀 방지
        Partner partner = product.getPartner();
        Partner simplePartner = new Partner();
        simplePartner.setUno(partner.getUno());
        simplePartner.setPname(partner.getPname());
        simplePartner.setCeoName(partner.getCeoName());
        simplePartner.setLicense(partner.getLicense());
        dto.setPartner(simplePartner);
        // fromEntity 수정
        dto.setProdImageNames(
                product.getImages().stream()
                        .map(ProductImage::getFileName)
                        .collect(Collectors.toList())
        );
        dto.setProdImageDataList(
                product.getImages().stream()
                        .map(ProductImage::getImageData)
                        .collect(Collectors.toList())
        );

        // LocalDateTime -> LocalDate 변환
        if (product.getCreatedAt() != null) {
            dto.setCreatedAt(product.getCreatedAt().toLocalDate());
        }
        
        // 썸네일 설정: 이미지가 있으면 첫 번째 이미지를 썸네일로 사용, 없으면 기존 prodThumbnail 사용
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            dto.setProdThumbnail(product.getImages().get(0).getFileName());
        } else {
            dto.setProdThumbnail(product.getProdThumbnail());
        }
        
        return dto;
    }

}