package kroryi.dagon.DTO.multtae;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SunRiseSetDTO {
    private String sunrise;
    private String sunset;
} 