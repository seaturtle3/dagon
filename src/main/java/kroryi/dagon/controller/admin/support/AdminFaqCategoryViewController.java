package kroryi.dagon.controller.admin.support;

import kroryi.dagon.DTO.board.FAQCategoryRequestDTO;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.service.support.FAQCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/faq-categories")
public class AdminFaqCategoryViewController {

    private final FAQCategoryService faqCategoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", faqCategoryService.findAll());
        model.addAttribute("categoryForm", new FAQCategoryRequestDTO());
        return "admin/faq/category-form";
    }

    @PostMapping
    public String save(@ModelAttribute("categoryForm") FAQCategoryRequestDTO dto) {
        if (dto.getId() == null) {
            faqCategoryService.save(new FAQCategory(dto.getName(), dto.getDisplayOrder()));
        } else {
            FAQCategory category = faqCategoryService.findById(dto.getId());
            category.setName(dto.getName());
            category.setDisplayOrder(dto.getDisplayOrder());
            faqCategoryService.save(category);
        }
        return "redirect:/admin/faq-categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        FAQCategory category = faqCategoryService.findById(id);
        model.addAttribute("categories", faqCategoryService.findAll());
        model.addAttribute("categoryForm", new FAQCategoryRequestDTO(category.getId(), category.getName(), category.getDisplayOrder()));
        return "admin/faq/category-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        faqCategoryService.delete(id);
        return "redirect:/admin/faq-categories";
    }
}