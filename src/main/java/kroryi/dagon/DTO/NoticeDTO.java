package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link kroryi.dagon.entity.Notice}
 */
@Value
public class NoticeDTO implements Serializable {
    String nid;
    String adminId;
    Instant createdAt;
    Instant modifyAt;
    String ncontent;
    String nnickname;
    String ntitle;
    Integer views;
}