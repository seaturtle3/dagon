package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.Event;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class EventResponseDTO {
    private Long eventId;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDate startAt;
    private LocalDate endAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifyAt;
    private String eventStatus;
    private Integer views;
    private Boolean isTop;
    private String adminName;

    public static EventResponseDTO from(Event e) {
        return EventResponseDTO.builder()
                .eventId(e.getEventId())
                .title(e.getTitle())
                .content(e.getContent())
                .thumbnailUrl(e.getThumbnailUrl())
                .startAt(e.getStartAt())
                .endAt(e.getEndAt())
                .createdAt(e.getCreatedAt())
                .modifyAt(e.getModifyAt())
                .eventStatus(e.getEventStatus().getKorean())
                .views(e.getViews())
                .isTop(e.getIsTop())
                .adminName(e.getAdmin().getAname())
                .build();
    }

}
