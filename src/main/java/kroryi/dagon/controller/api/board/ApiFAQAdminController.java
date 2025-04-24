package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.DTO.board.FAQResponseDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FAQ(관리자)", description = "관리자용 FAQ 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/faqs")
@PreAuthorize("hasRole('ADMIN')")
public class ApiFAQAdminController {

    private final FAQService faqService;

    @Operation(summary = "FAQ 목록 조회", description = "전체 FAQ 목록을 페이지 단위로 조회합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public Page<FAQResponseDTO> getPagedFAQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return faqService.findAllPaged(PageRequest.of(page, size))
                .map(FAQResponseDTO::from);
    }

    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID로 상세 내용을 조회합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping("/{id}")
    public FAQResponseDTO getOne(@PathVariable Long id) {
        return FAQResponseDTO.from(faqService.findById(id));
    }

    @Operation(summary = "FAQ 등록", description = "새로운 FAQ를 등록합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<FAQResponseDTO> create(@Valid @RequestBody FAQRequestDTO dto,
                                                 @AuthenticationPrincipal AdminUserDetails admin) {
        String aid = admin.getAid();
        FAQ faq = faqService.createFAQ(dto, aid);
        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 수정", description = "기존 FAQ를 수정합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> update(@PathVariable Long id,
                                                 @RequestBody FAQRequestDTO dto) {
        FAQ faq = faqService.updateFAQ(id, dto);
        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ ID를 기준으로 삭제합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }
}