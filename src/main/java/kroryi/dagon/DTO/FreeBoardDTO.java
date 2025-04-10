package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FreeBoard}
 */
@Value
public class FreeBoardDTO implements Serializable {
    Long id;
    String fbtitle;
    String fbcontent;
    Instant createdAt;
    Instant modify;
    Integer views;
}