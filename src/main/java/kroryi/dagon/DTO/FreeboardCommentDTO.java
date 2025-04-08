package kroryi.dagon.DTO;

import kroryi.dagon.entity.FreeBoardComment;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link FreeBoardComment}
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