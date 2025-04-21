package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.DTO.board.FAQResponseDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/faq")
public class ApiFAQAdminController {
    private final FAQService faqService;

    @Operation(summary = "FAQ 전체 목록 조회 (관리자)", description = "모든 FAQ를 조회 (비활성 포함).")
    @GetMapping
    public List<FAQResponseDTO> getAll() {
        return faqService.findAll().stream()
                .map(FAQResponseDTO::from)
                .toList();
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
