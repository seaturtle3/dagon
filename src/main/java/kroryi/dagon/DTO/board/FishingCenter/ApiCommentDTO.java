package kroryi.dagon.DTO.board.FishingCenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.entity.fishingCenter.FishingDiaryComment;
import kroryi.dagon.entity.fishingCenter.FishingReportComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiCommentDTO {
    private Long frCommentId;
    private String comment;
    private ApiUserDTO user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public ApiCommentDTO(FishingReportComment comment) {
        this.frCommentId = comment.getFrCommentId();
        this.comment = comment.getCommentContent();
        this.createdAt = comment.getCreatedAt();
    }

    public ApiCommentDTO(FishingDiaryComment comment) {
        this.frCommentId = comment.getFdCommentId();
        this.comment = comment.getCommentContent();
        this.user = new ApiUserDTO(comment.getUser());
        this.createdAt = comment.getCreatedAt();
    }
}