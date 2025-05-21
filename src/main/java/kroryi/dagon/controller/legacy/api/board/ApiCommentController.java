package kroryi.dagon.controller.legacy.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.CommentDTO;
import kroryi.dagon.service.board.fishingReportDiary.FishingDiaryCommentServiceImpl;
import kroryi.dagon.service.board.fishingReportDiary.FishingReportCommentServiceImpl;
import kroryi.dagon.service.FreeBoardCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Board-Comment", description = "게시판 댓글 작성/삭제 API")
public class ApiCommentController {

    private final FishingDiaryCommentServiceImpl fishingDiaryCommentService;
    private final FishingReportCommentServiceImpl fishingReportCommentService;
    private final FreeBoardCommentServiceImpl freeBoardCommentService;

    @Autowired
    public ApiCommentController(
            @Qualifier("fishingDiaryCommentServiceImpl") FishingDiaryCommentServiceImpl fishingDiaryCommentService,
            @Qualifier("fishingReportCommentServiceImpl") FishingReportCommentServiceImpl fishingReportCommentService,
            @Qualifier("freeBoardCommentServiceImpl") FreeBoardCommentServiceImpl freeBoardCommentService) {
        this.fishingDiaryCommentService = fishingDiaryCommentService;
        this.fishingReportCommentService = fishingReportCommentService;
        this.freeBoardCommentService = freeBoardCommentService;
    }

    // Fishing Diary 댓글 조회
    @GetMapping("/fishing-diary/{postId}")
    @Operation(summary = "조행기 댓글 조회", description = "조행기 댓글 조회")
    public ResponseEntity<List<CommentDTO>> getFishingDiaryComments(@PathVariable Long postId) {
        List<CommentDTO> comments = fishingDiaryCommentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    // Fishing Diary 댓글 작성
    @PostMapping("/fishing-diary/{postId}")
    @Operation(summary = "조행기 댓글 작성", description = "조행기 게시물에 댓글을 작성합니다.")
    public ResponseEntity<Void> createFishingDiaryComment(@PathVariable Long postId, @RequestParam String content, @RequestParam Long userId) {
        fishingDiaryCommentService.createComment(postId, content, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Fishing Diary 댓글 수정
    @PutMapping("/fishing-diary/{commentId}")
    @Operation(summary = "조행기 댓글 수정", description = "조행기 댓글의 내용을 수정합니다.")
    public ResponseEntity<Void> updateFishingDiaryComment(@PathVariable Long commentId, @RequestParam String content) {
        fishingDiaryCommentService.updateComment(commentId, content);
        return ResponseEntity.ok().build();
    }

    // Fishing Diary 댓글 삭제
    @DeleteMapping("/fishing-diary/{commentId}")
    @Operation(summary = "조행기 댓글 삭제", description = "조행기 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteFishingDiaryComment(@PathVariable Long commentId) {
        fishingDiaryCommentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Fishing Report 댓글 조회
    @GetMapping("/fishing-report/{postId}")
    @Operation(summary = "조황정보 댓글 조회", description = "특정 조황정보 게시물의 댓글을 조회합니다.")
    public ResponseEntity<List<CommentDTO>> getFishingReportComments(@PathVariable Long postId) {
        List<CommentDTO> comments = fishingReportCommentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    // Fishing Report 댓글 작성
    @PostMapping("/fishing-report/{postId}")
    @Operation(summary = "조황정보 댓글 작성", description = "조황정보 게시물에 댓글을 작성합니다.")
    public ResponseEntity<Void> createFishingReportComment(@PathVariable Long postId, @RequestParam String content, @RequestParam Long userId) {
        fishingReportCommentService.createComment(postId, content, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Fishing Report 댓글 수정
    @PutMapping("/fishing-report/{commentId}")
    @Operation(summary = "조황정보 댓글 수정", description = "조황정보 댓글의 내용을 수정합니다.")
    public ResponseEntity<Void> updateFishingReportComment(@PathVariable Long commentId, @RequestParam String content) {
        fishingReportCommentService.updateComment(commentId, content);
        return ResponseEntity.ok().build();
    }

    // Fishing Report 댓글 삭제
    @DeleteMapping("/fishing-report/{commentId}")
    @Operation(summary = "조황정보 댓글 삭제", description = "조황정보 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteFishingReportComment(@PathVariable Long commentId) {
        fishingReportCommentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Free Board 댓글 조회
    @GetMapping("/free-board/{postId}")
    @Operation(summary = "자유게시판 댓글 조회", description = "특정 자유게시판 게시물의 댓글을 조회합니다.")
    public ResponseEntity<List<CommentDTO>> getFreeBoardComments(@PathVariable Long postId) {
        List<CommentDTO> comments = freeBoardCommentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    // Free Board 댓글 작성
    @PostMapping("/free-board/{postId}")
    @Operation(summary = "자유게시판 댓글 작성", description = "자유게시판 게시물에 댓글을 작성합니다.")
    public ResponseEntity<Void> createFreeBoardComment(@PathVariable Long postId, @RequestParam String content, @RequestParam Long userId) {
        freeBoardCommentService.createComment(postId, content, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Free Board 댓글 수정
    @PutMapping("/free-board/{commentId}")
    @Operation(summary = "자유게시판 댓글 수정", description = "자유게시판 댓글의 내용을 수정합니다.")
    public ResponseEntity<Void> updateFreeBoardComment(@PathVariable Long commentId, @RequestParam String content) {
        freeBoardCommentService.updateComment(commentId, content);
        return ResponseEntity.ok().build();
    }

    // Free Board 댓글 삭제
    @DeleteMapping("/free-board/{commentId}")
    @Operation(summary = "자유게시판 댓글 삭제", description = "자유게시판 댓글을 삭제합니다.")
    public ResponseEntity<Void> deleteFreeBoardComment(@PathVariable Long commentId) {
        freeBoardCommentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}