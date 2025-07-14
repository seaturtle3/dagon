package kroryi.dagon.controller.admin.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.DTO.board.FAQResponseDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.support.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;


import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "FAQ", description = "FAQ 관리 API (관리자)")
@RequestMapping("/api/admin/faq")
public class ApiAdminFaqController {
    private final FAQService faqService;

    @Operation(summary = "FAQ 목록 페이징 조회 (관리자)", description = "FAQ 전체를 페이지 단위로 조회")
    @GetMapping
    public Page<FAQResponseDTO> getPagedFAQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String faqType,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isActive
    ) {
        System.out.println("👉 [FAQ 검색 요청] keyword = " + keyword + ", faqType = " + faqType + ", categoryId = " + categoryId);  // ✅ 이거 추가

        BoardSearchDTO dto = new BoardSearchDTO();
        dto.setKeyword(keyword);
        dto.setFaqType(faqType);
        dto.setCategoryId(categoryId);
        dto.setIsActive(isActive);

        Page<FAQ> faqPage = faqService.searchFaq(dto, PageRequest.of(page, size));
        return faqPage.map(FAQResponseDTO::from);
    }


    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID 단건 조회")
    @GetMapping("/{id}")
    public FAQResponseDTO getOne(@PathVariable Long id) {
        return FAQResponseDTO.from(faqService.findById(id));
    }

    @Operation(summary = "FAQ 등록", description = "새로운 FAQ 등록")
    @PostMapping
    public ResponseEntity<FAQResponseDTO> create(
            @Valid @RequestBody FAQRequestDTO dto, BindingResult result,
            @AuthenticationPrincipal AdminUserDetails userDetails) {

        String adminId = userDetails.getAid();
        FAQ faq = faqService.createFAQ(dto, adminId);

        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 수정", description = "기존 FAQ 내용 수정")
    @PutMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> update(
            @PathVariable Long id,
            @RequestBody FAQRequestDTO dto,
            @AuthenticationPrincipal AdminUserDetails userDetails) {

        String adminId = userDetails.getAid();
        FAQ faq = faqService.updateFAQ(id, dto, adminId);

        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserDetails userDetails) {

        String adminId = userDetails.getAid();
        faqService.deleteFAQ(id, adminId);
        return ResponseEntity.noContent().build();
    }

}
