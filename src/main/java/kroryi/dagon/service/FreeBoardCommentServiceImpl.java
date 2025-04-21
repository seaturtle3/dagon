package kroryi.dagon.service;

import kroryi.dagon.DTO.CommentDTO;
import kroryi.dagon.entity.FreeBoard;
import kroryi.dagon.entity.FreeBoardComment;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.FreeBoardCommentRepository;
import kroryi.dagon.repository.FreeBoardRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeBoardCommentServiceImpl implements CommentService {

    private final FreeBoardRepository freeBoardRepository;
    private final FreeBoardCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void createComment(Long postId, String content, Long userId) {
        FreeBoard board = freeBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        FreeBoardComment comment = new FreeBoardComment();
        comment.setCommentContent(content);
        comment.setFreeBoard(board);
        comment.setUser(user);


        commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByFreeBoard_FbId(postId)
                .stream()
                .map(c -> new CommentDTO(
                        c.getFbCommentId(),
                        c.getUser().getNickname(),
                        c.getCommentContent(),
                        c.getModifyAt()
                )).collect(Collectors.toList());
    }

    @Override
    public void updateComment(Long commentId, String content) {
        FreeBoardComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));
        comment.setCommentContent(content);
        comment.setModifyAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

