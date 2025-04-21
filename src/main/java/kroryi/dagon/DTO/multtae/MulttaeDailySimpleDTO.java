package kroryi.dagon.DTO.multtae;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MulttaeDailySimpleDTO {
    private LocalDate date;
    private String mulName;       // 1물~14물, 사리, 조금
    private String sunrise;       // "06:00"
    private String sunset;        // "18:59"
    private String windDir;       // "북북동풍"
    private Double windSpeed;     // 7.9 (m/s)
}