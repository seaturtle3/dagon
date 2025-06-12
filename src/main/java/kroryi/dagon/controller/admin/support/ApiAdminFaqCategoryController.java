package kroryi.dagon.controller.admin.support;

import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.service.support.FAQCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/faq-categories")
@Tag(name = "FAQ", description = "FAQ 카테고리 관리 API (관리자)")
public class ApiAdminFaqCategoryController {

    private final FAQCategoryService faqCategoryService;

    @GetMapping
    @Operation(summary = "FAQ 카테고리 목록 조회")
    public List<FAQCategory> getAll() {
        return faqCategoryService.findAll();
    }

    @PostMapping
    @Operation(summary = "FAQ 카테고리 등록")
    public FAQCategory create(@RequestBody FAQCategory category) {
        return faqCategoryService.save(category);
    }

    @PutMapping("/{id}")
    @Operation(summary = "FAQ 카테고리 수정")
    public FAQCategory update(@PathVariable Long id, @RequestBody FAQCategory request) {
        FAQCategory existing = faqCategoryService.findById(id);
        existing.setName(request.getName());
        existing.setDisplayOrder(request.getDisplayOrder());
        return faqCategoryService.save(existing);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "FAQ 카테고리 삭제")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
