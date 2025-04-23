package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.DTO.board.FAQResponseDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/faq")
public class ApiFAQAdminController {
    private final FAQService faqService;

    @Operation(summary = "FAQ 목록 페이징 조회 (관리자)", description = "FAQ 전체를 페이지 단위로 조회")
    @GetMapping
    public Page<FAQResponseDTO> getPagedFAQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FAQ> faqPage = faqService.findAllPaged(PageRequest.of(page, size));
        return faqPage.map(FAQResponseDTO::from);
    }


    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID 단건 조회")
    @GetMapping("/{id}")
    public FAQResponseDTO getOne(@PathVariable Long id) {
        return FAQResponseDTO.from(faqService.findById(id));
    }


    @Operation(summary = "FAQ 등록", description = "새로운 FAQ 등록")
    @PostMapping
    public ResponseEntity<FAQResponseDTO> create(@Valid @RequestBody FAQRequestDTO dto,
                                                 Authentication auth) {
        String aid = auth.getName(); // 로그인한 관리자 ID
        FAQ faq = faqService.createFAQ(dto, aid);
        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }


    @Operation(summary = "FAQ 수정", description = "기존 FAQ 내용 수정")
    @PostMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> update(@PathVariable Long id, FAQRequestDTO dto) {
        FAQ faq = faqService.updateFAQ(id, dto);
        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }

}
