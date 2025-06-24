package kroryi.dagon.DTO.board.FishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Data
@NoArgsConstructor
public class ApiFishingReportImageDTO {
    private String imageUrl;
    private boolean isThumbnail;
    private String imageData; // Base64 인코딩된 이미지 데이터

    public ApiFishingReportImageDTO(FishingReportImage image) {
        this.imageUrl = image.getImageUrl();
        this.isThumbnail = image.isThumbnail();
        // imageData가 null이 아니면 Base64 인코딩
        if (image.getImageData() != null) {
            this.imageData = Base64.getEncoder().encodeToString(image.getImageData());
        } else {
            this.imageData = null;
        }
    }
}

