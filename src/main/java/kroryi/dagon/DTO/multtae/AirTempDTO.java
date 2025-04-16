package kroryi.dagon.DTO.multtae;

import kroryi.dagon.util.HasRecordTime;
import lombok.Data;

@Data
public class AirTempDTO implements HasRecordTime {
    private String record_time;
    private String air_temp;
}