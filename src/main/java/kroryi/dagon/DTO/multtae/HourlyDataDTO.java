package kroryi.dagon.DTO.multtae;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HourlyDataDTO {
    private String time;
    private Double wave;
    private Double wind_speed;
    private String wind_dir;
    private Double air_temp;
    private Double tide_level;
}