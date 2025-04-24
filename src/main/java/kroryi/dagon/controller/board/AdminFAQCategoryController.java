package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FAQCategoryRequestDTO;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.service.board.FAQCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/faq-categories")
public class AdminFAQCategoryController {

    private final FAQCategoryService faqCategoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", faqCategoryService.findAll());
        model.addAttribute("categoryForm", new FAQCategoryRequestDTO());
        return "admin/faq/category-form";
    }

    @PostMapping
    public String create(@ModelAttribute("categoryForm") FAQCategoryRequestDTO dto) {
        faqCategoryService.save(new FAQCategory(dto.getName(), dto.getDisplayOrder()));
        return "redirect:/admin/faq-categories";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("categoryForm") FAQCategoryRequestDTO dto) {
        FAQCategory category = faqCategoryService.findById(id);
        category.setName(dto.getName());
        category.setDisplayOrder(dto.getDisplayOrder());
        faqCategoryService.save(category);
        return "redirect:/admin/faq-categories";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        faqCategoryService.delete(id);
        return "redirect:/admin/faq-categories";
    }
}