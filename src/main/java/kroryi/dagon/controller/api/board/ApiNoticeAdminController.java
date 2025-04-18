package kroryi.dagon.controller.api.board;

import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
public class ApiNoticeAdminController {

    private final NoticeService noticeService;

    @PostMapping
    public NoticeResponseDTO create(@RequestBody NoticeRequestDTO dto){
        String adminId = "admin001"; // 테스트용
        Notice notice = noticeService.createNotice(dto, adminId);

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

    @PostMapping("/{id}")
    public NoticeResponseDTO update(@PathVariable Long id, @RequestBody NoticeRequestDTO dto){
        Notice notice = noticeService.updateNotice(id, dto);

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        noticeService.deleteNotice(id);
    }

}
