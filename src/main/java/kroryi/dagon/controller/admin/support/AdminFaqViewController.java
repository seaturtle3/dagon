package kroryi.dagon.controller.admin.support;

import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.service.board.FAQCategoryService;
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
@RequestMapping("/admin/faq")
public class AdminFaqViewController {
    private final FAQService faqService;
    private final FAQCategoryService faqCategoryService;

    @GetMapping
    public String readAll(Model model,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String type,
                          @RequestParam(required = false) Long categoryId) {

        boolean isAdmin = true;

        BoardSearchDTO searchDTO = new BoardSearchDTO();
        searchDTO.setKeyword(keyword);
        searchDTO.setType(type);
        searchDTO.setFaqType(type);
        searchDTO.setCategoryId(categoryId);


        Page<FAQ> faqPage = isAdmin
                ? faqService.searchFaq(searchDTO, PageRequest.of(page, size))
                : faqService.searchActivePaged(searchDTO, PageRequest.of(page, size));

        int totalPages = faqPage.getTotalPages();
        if (page >= totalPages && totalPages > 0) {
            return "redirect:/admin/faq?page=" + (totalPages - 1) + "&size=" + size;
        }

        model.addAttribute("faqPage", faqPage);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(faqPage));
        model.addAttribute("size", size);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", faqCategoryService.findAll());
        return "board/faq/faq";
    }


    @GetMapping("/create")
    public String createForm(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {

        FAQRequestDTO dto = new FAQRequestDTO();
        dto.setIsActive(true);
        model.addAttribute("faq", dto);
        model.addAttribute("formAction", "/admin/faq");
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("categories", faqCategoryService.findAll());
        return "board/faq/form";
    }

    @PostMapping
    public String create(@ModelAttribute FAQRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        faqService.createFAQ(dto, "admin001");
        return "redirect:/admin/faq?page=" + page + "&size=" + size;
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        FAQ faq = faqService.findById(id);
        model.addAttribute("faq", FAQRequestDTO.from(faq));
        model.addAttribute("formAction", "/admin/faq/" + id + "?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("categories", faqCategoryService.findAll());
        return "board/faq/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute FAQRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        faqService.updateFAQ(id, dto);
        return "redirect:/admin/faq?page=" + page + "&size=" + size;
    }
}
