package kroryi.dagon.DTO.board.FishingReportDiary;

import kroryi.dagon.entity.FishingReportComment;
import kroryi.dagon.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiCommentDTO {
    private Long frCommentId;
    private String comment;
    private ApiUserDTO user;

    public ApiCommentDTO(FishingReportComment comment) {
        this.frCommentId = comment.getFrCommentId();
        this.comment = comment.getCommentContent();

    }
}