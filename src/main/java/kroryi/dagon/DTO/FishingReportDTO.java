package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FishingReport}
 */
@Value
public class FishingReportDTO implements Serializable {
    Long id;
    String frcontent;
    Instant createAt;
    Instant fishingAt;
    Instant modifyAt;
    String frtitle;
    Integer views;
}