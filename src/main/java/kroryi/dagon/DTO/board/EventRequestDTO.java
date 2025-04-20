package kroryi.dagon.DTO.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDTO {
    @NotBlank(message = "제목은 필수")
    private String title;

    @NotBlank(message = "내용이 없습니다")
    private String content;
    private String thumbnailUrl;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean isTop;

}
