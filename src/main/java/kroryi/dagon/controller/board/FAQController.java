package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    public String showFAQ(Model model) {

        // 임시 관리자 모드 설정 (true로 하면 관리자처럼 보임)
        boolean isAdmin = true; // 이후 로그인 연동 시 실제 관리자 여부로 대체

        List<FAQ> faqs = isAdmin
                ? faqService.findAll()
                : faqService.findActiveOnly();
        model.addAttribute("faqList", faqs);
        model.addAttribute("isAdmin", isAdmin); // 뷰에서 분기 처리용
        return "board/faq/faq";
    }

    @GetMapping("/form")
    public String createForm(Model model) {
        model.addAttribute("faq", new FAQRequestDTO());
        model.addAttribute("formAction", "/api/admin/faq");
        return "board/faq/form";
    }

    @GetMapping("/form/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        FAQ faq = faqService.findById(id);
        model.addAttribute("faq", FAQRequestDTO.from(faq));
        model.addAttribute("formAction", "/api/admin/faq/" + faq.getFaqId());
        return "board/faq/form";
    }
}
