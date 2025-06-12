package kroryi.dagon.DTO.board.FishingReportDiary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiFishingCenterDTO {
    private List<ApiFishingReportDTO> reportList;
    private List<ApiFishingDiaryDTO> diaryList;
}

