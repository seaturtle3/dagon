package kroryi.dagon.controller.api.board;

import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class ApiNoticePublicController {

    private final NoticeService noticeService;

    @GetMapping
    public List<NoticeResponseDTO> getAll() {
        return noticeService.findAll().stream().map(notice -> NoticeResponseDTO.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .thumbnailUrl(notice.getThumbnailUrl())
                .createdAt(notice.getCreatedAt())
                .modifyAt(notice.getModifyAt())
                .views(notice.getViews())
                .isTop(notice.getIsTop())
                .adminName(notice.getAdmin().getAname())
                .build()).toList();
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
