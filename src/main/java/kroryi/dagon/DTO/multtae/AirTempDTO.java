package kroryi.dagon.DTO.multtae;

import lombok.Data;

@Data
public class AirTempDTO implements HasRecordTime  {
    private String record_time;
    private Double air_temp;
}