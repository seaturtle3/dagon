package kroryi.dagon.DTO.board.FishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiFishingReportImageDTO {
    private String imageUrl;
    private boolean isThumbnail;

    public ApiFishingReportImageDTO(FishingReportImage image) {
        this.imageUrl = image.getImageUrl();
        this.isThumbnail = image.isThumbnail();
    }
}

