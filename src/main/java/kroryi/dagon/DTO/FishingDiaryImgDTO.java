package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FishingDiaryImg}
 */
@Value
public class FishingDiaryImgDTO implements Serializable {
    Long id;
    String fdimgUrl;
    Instant uploadedAt;
}