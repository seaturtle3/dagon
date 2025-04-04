package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link kroryi.dagon.entity.FishingReportImg}
 */
@Value
public class FishingReportImgDTO implements Serializable {
    Long id;
    String frimgUrl;
    LocalDateTime uploadedAt;
}