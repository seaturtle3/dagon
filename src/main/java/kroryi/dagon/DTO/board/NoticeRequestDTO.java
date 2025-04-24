package kroryi.dagon.DTO.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoticeRequestDTO {

    @NotBlank(message = "제목은 필수")
    private String title;

    @NotBlank(message = "내용이 없습니다")
    private String content;
    private String thumbnailUrl;
    private Boolean isTop;

}
