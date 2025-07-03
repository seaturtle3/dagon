package kroryi.dagon.DTO.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class EventRequestDTO {

    @NotBlank(message = "제목은 필수")
    private String title;

    @NotBlank(message = "내용이 없습니다")
    private String content;
    private String thumbnailUrl;
    private LocalDate startAt;
    private LocalDate endAt;
    private Boolean isTop;
    private List<MultipartFile> images;

}
