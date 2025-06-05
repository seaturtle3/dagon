package kroryi.dagon.controller.admin.support;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.DTO.board.FAQResponseDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.support.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
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
            @RequestParam(required = false) String faqType
    ) {
        System.out.println("👉 [FAQ 검색 요청] keyword = " + keyword + ", faqType = " + faqType);  // ✅ 이거 추가


        BoardSearchDTO dto = new BoardSearchDTO();
        dto.setKeyword(keyword);
        dto.setFaqType(faqType);

        Page<FAQ> faqPage = faqService.searchFaq(dto,PageRequest.of(page, size));
        return faqPage.map(FAQResponseDTO::from);
    }


    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID 단건 조회")
    @GetMapping("/{id}")
    public FAQResponseDTO getOne(@PathVariable Long id) {
        return FAQResponseDTO.from(faqService.findById(id));
    }

    @Operation(summary = "FAQ 등록", description = "새로운 FAQ 등록")
    @PostMapping
    public ResponseEntity<FAQResponseDTO> create(@Valid FAQRequestDTO dto) {
        FAQ faq = faqService.createFAQ(dto, "admin001"); // 관리자 ID는 테스트용
        return ResponseEntity.ok(FAQResponseDTO.from(faq));
    }

    @Operation(summary = "FAQ 수정", description = "기존 FAQ 내용 수정")
    @PutMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> update(@PathVariable Long id,@RequestBody FAQRequestDTO dto) {
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
