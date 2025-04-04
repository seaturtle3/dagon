package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Fishingreportcomment}
 */
@Value
public class FishingreportcommentDTO implements Serializable {
    Long id;
    String comentContent;
    Instant createAt;
    Instant modifyAt;
}