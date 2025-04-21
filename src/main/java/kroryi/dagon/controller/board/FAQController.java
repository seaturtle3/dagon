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
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FAQController {
    private final FAQService faqService;

    @GetMapping
    public String readAll(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size) {

        boolean isAdmin = true;

        Page<FAQ> pageForCount = isAdmin
                ? faqService.findAllPaged(PageRequest.of(0, size))
                : faqService.findActivePaged(PageRequest.of(0, size));

        int totalPages = pageForCount.getTotalPages();
        if (page >= totalPages && totalPages > 0) {
            return "redirect:/faq?page=" + (totalPages - 1) + "&size=" + size;
        }

        Page<FAQ> faqPage = isAdmin
                ? faqService.findAllPaged(PageRequest.of(page, size))
                : faqService.findActivePaged(PageRequest.of(page, size));

        model.addAttribute("faqPage", faqPage);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(faqPage));
        model.addAttribute("size", size);
        model.addAttribute("isAdmin", isAdmin);
        return "board/faq/faq";
    }


    @GetMapping("/create")
    public String createForm(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("faq", new FAQRequestDTO());
        model.addAttribute("formAction", "/faq?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/faq/form";
    }

    @PostMapping
    public String create(@ModelAttribute FAQRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        faqService.createFAQ(dto, "admin001");
        return "redirect:/faq?page=" + page + "&size=" + size;
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        FAQ faq = faqService.findById(id);
        model.addAttribute("faq", FAQRequestDTO.from(faq));
        model.addAttribute("formAction", "/faq/" + id + "?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/faq/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute FAQRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        faqService.updateFAQ(id, dto);
        return "redirect:/faq?page=" + page + "&size=" + size;
    }
}
