package kroryi.dagon.DTO.board;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponseDTO {
    private Long eventId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String eventStatus;
    private Integer views;
    private Boolean isTop;
    private String adminName;
}
