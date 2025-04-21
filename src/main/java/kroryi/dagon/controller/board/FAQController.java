package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.service.board.FAQService;
import kroryi.dagon.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    public String showFAQ(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size) {

        boolean isAdmin = true;

        Page<FAQ> faqPage = isAdmin
                ? faqService.findAllPaged(PageRequest.of(page, size))
                : faqService.findActivePaged(PageRequest.of(page, size));

        model.addAttribute("faqPage", faqPage);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(faqPage));
        model.addAttribute("isAdmin", isAdmin);
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
