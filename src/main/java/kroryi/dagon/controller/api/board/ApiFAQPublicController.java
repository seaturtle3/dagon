package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FAQListDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board-FAQ", description = "사용자 FAQ 조회 API")
@RequestMapping("/api/faq")
public class ApiFAQPublicController {

    private final FAQService faqService;

    @GetMapping
    @Operation(summary = "FAQ 목록 조회 (사용자)", description = "활성화된 FAQ만 노출 + 페이징")
    public Page<FAQListDTO> getPublicFAQ(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<FAQ> faqPage = faqService.findActivePaged(PageRequest.of(page, size));
        return faqPage.map(FAQListDTO::from);
    }
}
