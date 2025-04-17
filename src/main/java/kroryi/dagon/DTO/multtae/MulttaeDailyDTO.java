package kroryi.dagon.DTO.multtae;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MulttaeDailyDTO {
    private LocalDate date;
    private Double lunarAge;
    private String mulName;
    private List<TideItemDTO> tideItems;
    private List<WaveDTO> waveList;
    private List<WindDTO> windList;
    private List<AirTempDTO> airTempList;
    private List<TideLevelDTO> tideLevelList;
}
