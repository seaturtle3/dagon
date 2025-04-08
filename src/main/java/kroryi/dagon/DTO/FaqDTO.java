package kroryi.dagon.DTO;

import kroryi.dagon.entity.FAQ;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link FAQ}
 */
@Value
public class FaqDTO implements Serializable {
    Long id;
    Long aid;
    String answer;
    Instant createdAt;
    Instant modifyAt;
    String question;
}