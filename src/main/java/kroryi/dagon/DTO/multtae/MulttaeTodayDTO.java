package kroryi.dagon.DTO.multtae;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MulttaeTodayDTO {
    private LocalDate date;
    private String stationCode;
    private String stationName;

    private String mulName;
    private Double lunarAge;

    private String sunrise;
    private String sunset;

    private String weatherNow;
    private String temperature;

    private List<TideItemDTO> tideItems;

    private List<WaveDTO> waveList;
    private List<WindDTO> windList;
    private List<AirTempDTO> airTempList;
    private List<TideLevelDTO> tideLevelList;

}
