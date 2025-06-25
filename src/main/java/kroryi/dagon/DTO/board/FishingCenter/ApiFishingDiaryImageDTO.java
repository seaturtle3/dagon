package kroryi.dagon.DTO.board.FishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFishingDiaryImageDTO {
    private List<FishingReportImage> images = new ArrayList<>();

    public ApiFishingDiaryImageDTO(FishingDiaryImage fishingDiaryImage) {
    }
}

