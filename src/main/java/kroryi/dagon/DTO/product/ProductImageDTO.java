package kroryi.dagon.DTO.product;

import kroryi.dagon.entity.product.ProductImage;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Data
@NoArgsConstructor
public class ProductImageDTO {
    private String imageUrl;
    private boolean thumbnail; // 썸네일 여부
    private String imageData; // Base64 인코딩된 이미지 데이터
    private String thumbnailData; // Base64 인코딩된 썸네일 데이터

    public ProductImageDTO(ProductImage image) {
        this.imageUrl = image.getFileName();
        this.thumbnail = false; // 상품 이미지는 기본적으로 썸네일이 아님 (첫 번째 이미지가 대표)
        
        // imageData가 null이 아니면 Base64 인코딩
        if (image.getImageData() != null) {
            this.imageData = Base64.getEncoder().encodeToString(image.getImageData());
            // 썸네일 데이터는 원본과 동일 (상품은 별도 썸네일 생성 안함)
            this.thumbnailData = Base64.getEncoder().encodeToString(image.getImageData());
        } else {
            this.imageData = null;
            this.thumbnailData = null;
        }
    }
} 