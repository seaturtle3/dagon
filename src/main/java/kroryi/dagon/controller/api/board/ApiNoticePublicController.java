package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공지사항", description = "사용자용 공지사항 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class ApiNoticePublicController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 목록 조회", description = "전체 공지사항을 페이징하여 조회합니다.")
    @GetMapping
    public Page<NoticeResponseDTO> getAllPaged(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeService.findAllPaged(pageable)
                .map(NoticeResponseDTO::from);
    }

    @Operation(summary = "공지사항 단건 조회", description = "공지사항 상세 정보를 조회하고, 조회수를 증가시킵니다.")
    @GetMapping("/{id}")
    public NoticeResponseDTO getOne(@PathVariable Long id) {
        noticeService.increaseViews(id);
        Notice notice = noticeService.findById(id);
        return NoticeResponseDTO.from(notice);
    }
}