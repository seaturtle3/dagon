package kroryi.dagon.controller.admin.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.support.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notice", description = "공지사항 관리 API (관리자)")
@RequestMapping("/api/admin/notices")
public class ApiAdminNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 등록", description = "관리자가 새 공지사항 등록")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NoticeRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String adminId = "admin001"; // 테스트용
        Notice notice = noticeService.createNotice(dto, adminId);

        return ResponseEntity.ok(NoticeResponseDTO.from(notice));
    }

    @Operation(summary = "공지사항 수정", description = "기존 공지사항 내용 수정")
    @PostMapping("/{id}")
    public NoticeResponseDTO update(@PathVariable Long id, @RequestBody NoticeRequestDTO dto){
        Notice notice = noticeService.updateNotice(id, dto);

        return NoticeResponseDTO.from(notice);
    }

    @Operation(summary = "공지사항 삭제", description = "해당 공지사항을 삭제")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        noticeService.deleteNotice(id);
    }

}
