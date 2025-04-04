package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.FreeBoardImage}
 */
@Value
public class FreeBoardImageDTO implements Serializable {
    Long id;
    String fdimgUrl;
    Instant uploadedAt;
}