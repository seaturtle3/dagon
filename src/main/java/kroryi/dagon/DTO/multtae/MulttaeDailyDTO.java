package kroryi.dagon.DTO.multtae;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class MulttaeDailyDTO {
    private LocalDate date;
    private String stationCode;
    private String stationName;

    private String sunrise;
    private String sunset;

    private Double lunarAge;
    private String mulName;

    private List<TideItemDTO> tideItems;
    private List<HourlyDataDTO> hourlyData;
}