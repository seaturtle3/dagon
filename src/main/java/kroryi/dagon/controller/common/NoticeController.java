package kroryi.dagon.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 조회 API (사용자)")
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록 조회", description = "전체 공지사항 페이징 방식으로 조회")
    @GetMapping
    public Page<NoticeResponseDTO> getAllPaged(
            @ModelAttribute BoardSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeService.searchNotices(searchDTO,pageable)
                .map(NoticeResponseDTO::from);
    }

    @Operation(summary = "공지사항 단건 조회", description = "공지사항 상세 내용 조회하고 조회수를 1 증가")
    @GetMapping("/{id}")
    public NoticeResponseDTO getOne(@PathVariable Long id) {
        noticeService.increaseViews(id);
        Notice notice = noticeService.findById(id);

        return NoticeResponseDTO.from(notice);
    }

}
