package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.Notice;
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

    public static NoticeResponseDTO from(Notice notice) {
        return NoticeResponseDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .thumbnailUrl(notice.getThumbnailUrl())
                .createdAt(notice.getCreatedAt())
                .modifyAt(notice.getModifyAt())
                .views(notice.getViews())
                .isTop(notice.getIsTop())
                .adminName(notice.getAdmin().getAname())
                .build();
    }
}
