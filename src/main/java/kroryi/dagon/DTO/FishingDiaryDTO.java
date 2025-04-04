package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FishingDiary}
 */
@Value
public class FishingDiaryDTO implements Serializable {
    Long id;
    Instant createAt;
    String fdcontent;
    String fdtitle;
    Instant fishingAt;
    Instant modifyAt;
    Integer views;
}