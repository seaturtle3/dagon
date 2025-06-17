package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;


@Data
public class FishingReportCreateDTO {
    private Long prodId;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")  // JSON에서 날짜 형식 지정
    private LocalDate fishingAt;
}
