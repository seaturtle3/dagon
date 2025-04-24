package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FAQListDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FAQ", description = "사용자용 FAQ 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
public class ApiFAQPublicController {

    private final FAQService faqService;

    @Operation(summary = "FAQ 목록 조회", description = "활성화된 FAQ만 페이징하여 조회합니다.")
    @GetMapping
    public Page<FAQListDTO> getPublicFAQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<FAQ> faqPage = faqService.findActivePaged(PageRequest.of(page, size));
        return faqPage.map(FAQListDTO::from);
    }

    @Operation(summary = "FAQ 단건 조회", description = "FAQ ID로 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public FAQListDTO getOne(@PathVariable Long id) {
        return FAQListDTO.from(faqService.findById(id));
    }
}