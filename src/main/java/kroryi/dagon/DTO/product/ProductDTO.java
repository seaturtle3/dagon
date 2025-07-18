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
import java.util.Base64;
import kroryi.dagon.DTO.product.ProductImageDTO;

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
    private List<String> prodImageDataList; // Base64 인코딩된 이미지 데이터 리스트
    
    // 홈페이지용 필드 (조황센터/조행기와 동일한 구조)
    private String thumbnailData; // Base64 인코딩된 썸네일 데이터
    private String imageData; // Base64 인코딩된 이미지 데이터
    private String thumbnailUrl; // 썸네일 URL
    private String imageUrl; // 이미지 URL
    
    // images 배열 추가 (조황센터/조행기와 동일한 구조)
    private List<ProductImageDTO> images;

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
                        .map(image -> {
                            if (image.getImageData() != null) {
                                return Base64.getEncoder().encodeToString(image.getImageData());
                            }
                            return null;
                        })
                        .filter(data -> data != null)
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

    // 홈페이지용 생성자 (이미지 데이터 제외)
    public static ProductDTO fromEntityForHome(Product product) {
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
        
        // 이미지 처리 (조황센터/조행기와 동일한 방식)
        if (product.getImages() != null && !product.getImages().isEmpty()) {
            // 첫 번째 이미지를 대표 이미지로 사용
            ProductImage firstImage = product.getImages().get(0);
            
            // 이미지 URL 설정
            dto.setImageUrl(firstImage.getFileName());
            dto.setThumbnailUrl(firstImage.getFileName());
            
            // 이미지 데이터 설정 (Base64)
            if (firstImage.getImageData() != null) {
                dto.setImageData(Base64.getEncoder().encodeToString(firstImage.getImageData()));
                dto.setThumbnailData(Base64.getEncoder().encodeToString(firstImage.getImageData())); // 썸네일은 원본과 동일
            }
            
            // images 배열 설정 (조황센터/조행기와 동일한 구조)
            dto.setImages(product.getImages().stream()
                    .map(ProductImageDTO::new)
                    .collect(Collectors.toList()));
            
            // 기존 필드도 설정
            dto.setProdImageNames(
                    product.getImages().stream()
                            .map(ProductImage::getFileName)
                            .collect(Collectors.toList())
            );
        }
        
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