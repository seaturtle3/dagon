package kroryi.dagon.DTO.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoticeResponseDTO {
    private Long noticeId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifyAt;
    private Integer views;
    private Boolean isTop;
    private String adminName;
}
