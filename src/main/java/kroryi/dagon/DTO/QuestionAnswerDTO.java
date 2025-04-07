package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.QuestionAnswer}
 */
@Value
public class QuestionAnswerDTO implements Serializable {
    Long id;
    Long answerId;
    Instant createdAt;
    String qacontent;
    Long qidQid;
}