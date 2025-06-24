package kroryi.dagon.DTO;

import kroryi.dagon.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private String writerNickname;
    private String content;
    private LocalDateTime createdAt;
    private UsersDTO user;

}
