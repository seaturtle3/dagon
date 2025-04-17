package kroryi.dagon.DTO.multtae;

import lombok.Data;

@Data
public class TideLevelDTO implements HasRecordTime {
    private String record_time;
    private String tide_level;
}