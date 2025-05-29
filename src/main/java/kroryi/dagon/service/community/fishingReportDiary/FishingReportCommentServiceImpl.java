package kroryi.dagon.service.community.fishingReportDiary;

import kroryi.dagon.DTO.CommentDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.FishingReportComment;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.FishingReportCommentRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingReportCommentServiceImpl implements CommentService {

    private final FishingReportRepository reportRepository;
    private final FishingReportCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public void createComment(Long postId, String content, Long userId) {
        FishingReport report = reportRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        FishingReportComment comment = new FishingReportComment();
        comment.setCommentContent(content);
        comment.setFishingReport(report);
        comment.setUser(user);
        comment.setModifyAt(LocalDateTime.now());


        commentRepository.save(comment);
    }

    @Override
    public List<CommentDTO> getComments(Long postId) {
        return commentRepository.findByFishingReport_FrId(postId)
                .stream()
                .map(c -> new CommentDTO(
                        c.getFrCommentId(),
                        c.getUser().getNickname(),
                        c.getCommentContent(),
                        c.getModifyAt()
                )).collect(Collectors.toList());
    }

    @Override
    public void updateComment(Long commentId, String content) {
        FishingReportComment comment = commentRepository.findById(commentId)
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
