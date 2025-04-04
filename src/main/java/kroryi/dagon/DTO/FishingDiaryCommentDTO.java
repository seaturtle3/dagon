package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FishingDiaryComment}
 */
@Value
public class FishingDiaryCommentDTO implements Serializable {
    Long id;
    String commentContent;
    Instant createAt;
    Instant modifyAt;
}