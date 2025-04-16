package kroryi.dagon.DTO.multtae;

import kroryi.dagon.util.HasRecordTime;
import lombok.Data;

@Data
public class WaveDTO implements HasRecordTime {
    private String record_time;
    private String wave_height;
}