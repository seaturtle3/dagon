package kroryi.dagon.service.board.fishingReportDiary;


import kroryi.dagon.DTO.CommentDTO;
import kroryi.dagon.entity.FishingDiary;
import kroryi.dagon.entity.FishingDiaryComment;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.FishingDiaryCommentRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingDiaryCommentServiceImpl implements CommentService {

    private final FishingDiaryRepository diaryRepository;
    private final FishingDiaryCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void createComment(Long postId, String content, Long userId) {
        FishingDiary diary = diaryRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        FishingDiaryComment comment = new FishingDiaryComment();
        comment.setCommentContent(content);
        comment.setFishingDiary(diary);
        comment.setUser(user);
        comment.setModifyAt(LocalDateTime.now());

        commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByFishingDiary_FdId(postId)
                .stream()
                .map(c -> new CommentDTO(
                        c.getFdCommentId(),
                        c.getUser().getNickname(),
                        c.getCommentContent(),
                        c.getModifyAt()
                )).collect(Collectors.toList());
    }

    @Override
    public void updateComment(Long commentId, String content) {
        FishingDiaryComment comment = commentRepository.findById(commentId)
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

