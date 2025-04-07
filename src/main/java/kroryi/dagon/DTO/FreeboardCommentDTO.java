package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FreeboardComment}
 */
@Value
public class FreeboardCommentDTO implements Serializable {
    Long id;
    Long uid;
    Integer ulevel;
    String unickname;
    String comentContentt;
    Instant modifyAt;
}