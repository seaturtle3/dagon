package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
public class ApiFishingDiaryImageDTO {
    private String imageUrl;
    private boolean isThumbnail;

    @JsonProperty("imageData")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String imageData;

    @JsonProperty("thumbnailData")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private String thumbnailData;

    public ApiFishingDiaryImageDTO(FishingDiaryImage image) {
        this.imageUrl = image.getImageUrl();
        this.isThumbnail = image.isThumbnail();
        // imageData가 null이 아니면 Base64 인코딩
        if (image.getImageData() != null) {
            this.imageData = Base64.getEncoder().encodeToString(image.getImageData());
        } else {
            this.imageData = null;
        }
        if (image.getThumbnailData() != null) {
            this.thumbnailData = Base64.getEncoder().encodeToString(image.getThumbnailData());
        } else {
            this.thumbnailData = null;
        }
    }
}

