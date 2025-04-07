package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Question}
 */
@Value
public class QuestionDTO implements Serializable {
    Long id;
    String qtype;
    Long uid;
    Instant createdAt;
    String qcontent;
    String qtitle;
    Instant updatedAt;
    String usertype;
}