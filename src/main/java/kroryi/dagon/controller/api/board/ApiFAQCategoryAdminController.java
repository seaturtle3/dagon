package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FAQCategoryRequestDTO;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.service.board.FAQCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FAQ 카테고리(관리자)", description = "관리자용 FAQ 카테고리 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/faq-categories")
@PreAuthorize("hasRole('ADMIN')")
public class ApiFAQCategoryAdminController {

    private final FAQCategoryService faqCategoryService;

    @Operation(summary = "FAQ 카테고리 목록 조회", description = "전체 FAQ 카테고리를 조회합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @GetMapping
    public List<FAQCategory> getAll() {
        return faqCategoryService.findAll();
    }

    @Operation(summary = "FAQ 카테고리 등록", description = "새로운 FAQ 카테고리를 등록합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<FAQCategory> create(@RequestBody FAQCategoryRequestDTO dto) {
        FAQCategory category = new FAQCategory(dto.getName(), dto.getDisplayOrder());
        return ResponseEntity.ok(faqCategoryService.save(category));
    }

    @Operation(summary = "FAQ 카테고리 수정", description = "카테고리 ID를 기준으로 이름과 정렬 순서를 수정합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<FAQCategory> update(@PathVariable Long id,
                                              @RequestBody FAQCategoryRequestDTO dto) {
        FAQCategory category = faqCategoryService.findById(id);
        category.setName(dto.getName());
        category.setDisplayOrder(dto.getDisplayOrder());
        return ResponseEntity.ok(faqCategoryService.save(category));
    }

    @Operation(summary = "FAQ 카테고리 삭제", description = "카테고리 ID를 기준으로 삭제합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        faqCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
