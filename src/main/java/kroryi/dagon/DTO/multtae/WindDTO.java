package kroryi.dagon.DTO.multtae;

import lombok.Data;

@Data
public class WindDTO implements HasRecordTime {
    private String record_time;
    private String wind_speed;
    private String wind_dir;
}