package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.board.FAQListDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faq")
public class ApiFAQPublicController {

    private final FAQService faqService;

    @Operation(summary = "FAQ 목록 조회 (사용자)", description = "활성화된 FAQ만 노출")
    @GetMapping
    public List<FAQListDTO> getPublicFAQ() {
        return faqService.findActiveOnly().stream()
                .map(FAQListDTO::from)
                .toList();
    }
}
