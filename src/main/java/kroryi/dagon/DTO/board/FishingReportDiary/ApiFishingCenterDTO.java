package kroryi.dagon.DTO.board.FishingReportDiary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiFishingCenterDTO {

    private ApiFishingReportDTO fishingReport;
    private ApiFishingDiaryDTO fishingDiary;
}

