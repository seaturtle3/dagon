package kroryi.dagon.DTO.multtae;

import lombok.Data;

@Data
public class WaveDTO implements HasRecordTime {
    private String record_time;
    private Double wave_height;
}