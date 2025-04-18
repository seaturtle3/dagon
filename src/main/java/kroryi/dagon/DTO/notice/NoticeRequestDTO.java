package kroryi.dagon.DTO.notice;

import lombok.Data;

@Data
public class NoticeRequestDTO {
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isTop;

}
