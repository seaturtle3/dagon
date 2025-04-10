package kroryi.dagon.DTO;

import kroryi.dagon.entity.FishingReportComment;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link FishingReportComment}
 */
@Value
public class FishingreportcommentDTO implements Serializable {
    Long id;
    String comentContent;
    Instant createAt;
    Instant modifyAt;
}