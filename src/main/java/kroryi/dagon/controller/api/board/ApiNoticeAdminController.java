package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.DTO.board.NoticeResponseDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/notices")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "공지사항(관리자)", description = "관리자용 공지사항 등록/수정/삭제 API")
public class ApiNoticeAdminController {

    private final NoticeService noticeService;

    @Operation(
            summary = "공지사항 등록",
            description = "관리자가 새 공지사항을 등록합니다.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody NoticeRequestDTO dto,
                                    BindingResult result,
                                    @AuthenticationPrincipal AdminUserDetails admin) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        String aid = admin.getAid();
        Notice notice = noticeService.createNotice(dto, aid);
        return ResponseEntity.ok(NoticeResponseDTO.from(notice));
    }

    @Operation(
            summary = "공지사항 수정",
            description = "공지사항 ID를 기준으로 내용을 수정합니다.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/{id}")
    public NoticeResponseDTO update(@PathVariable Long id,
                                    @RequestBody NoticeRequestDTO dto) {
        Notice notice = noticeService.updateNotice(id, dto);
        return NoticeResponseDTO.from(notice);
    }

    @Operation(
            summary = "공지사항 삭제",
            description = "공지사항 ID를 기준으로 해당 공지를 삭제합니다.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
    }
}
