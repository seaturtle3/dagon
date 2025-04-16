package kroryi.dagon.DTO;

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
}
