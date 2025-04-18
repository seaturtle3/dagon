package kroryi.dagon.service;

import kroryi.dagon.DTO.CommentDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CommentService {
    void createComment(Long postId, String content, Long userId);
    List<CommentDTO> getComments(Long postId);
    void updateComment(Long commentId, String content);
    void deleteComment(Long commentId);
}
