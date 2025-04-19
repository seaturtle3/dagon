package kroryi.dagon.controller.api.board;

import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class ApiNoticePublicController {

    private final NoticeService noticeService;

    @GetMapping
    public Page<NoticeResponseDTO> getAllPaged(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeService.findAllPaged(pageable)
                .map(notice -> NoticeResponseDTO.builder()
                        .noticeId(notice.getNoticeId())
                        .title(notice.getTitle())
                        .content(notice.getContent())
                        .thumbnailUrl(notice.getThumbnailUrl())
                        .createdAt(notice.getCreatedAt())
                        .modifyAt(notice.getModifyAt())
                        .views(notice.getViews())
                        .isTop(notice.getIsTop())
                        .adminName(notice.getAdmin().getAname())
                        .build()
                );
    }

    @GetMapping("/{id}")
    public NoticeResponseDTO getOne(@PathVariable Long id){
        noticeService.increaseViews(id);
        Notice notice = noticeService.findById(id);

        return NoticeResponseDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .thumbnailUrl(notice.getThumbnailUrl())
                .createdAt(notice.getCreatedAt())
                .modifyAt(notice.getModifyAt())
                .views(notice.getViews())
                .isTop(notice.getIsTop())
                .adminName(notice.getAdmin().getAname())
                .build();
    }


}
