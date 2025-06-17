package kroryi.dagon.DTO.board.FishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiFishingDiaryImageDTO {
    private String imageUrl;
    private boolean isThumbnail;

    public ApiFishingDiaryImageDTO(FishingDiaryImage image) {
        this.imageUrl = image.getImageUrl();
        this.isThumbnail = image.isThumbnail();
    }
}

