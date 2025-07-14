package kroryi.dagon.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.product.ProductDTO;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Product", description = "관리자 제품 관리 API")
@RequestMapping("/api/admin/products")
@Log4j2
@CrossOrigin(origins = "http://localhost:5173")
public class ApiAdminProductController {

    private final ProductService productService;

    @Operation(summary = "전체 제품 목록 조회 (관리자)", description = "페이징으로 모든 제품 조회")
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) MainType mainType,
            @RequestParam(required = false) ProdRegion region,
            @RequestParam(required = false) Boolean deleted) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ProductDTO> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProductsByKeyword(search, pageable);
        } else if (mainType != null) {
            products = productService.getProductsByMainType(mainType, pageable);
        } else if (region != null) {
            products = productService.getProductsByRegion(region, pageable);
        } else if (deleted != null) {
            products = productService.getProductsByDeletedStatus(deleted, pageable);
        } else {
            products = productService.getAllProductsApi(pageable);
        }

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "제품 상세 조회 (관리자)", description = "ID로 제품 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "제품 등록 (관리자)", description = "새로운 제품 등록")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("dto") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            // 관리자용으로 기본 파트너 설정
            productService.createProductWithImages(productDTO, 1L, images);
            return ResponseEntity.ok("제품 등록이 완료되었습니다.");
        } catch (Exception e) {
            log.error("제품 등록 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제품 등록 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "제품 수정 (관리자)", description = "제품 정보 수정")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestPart("dto") ProductDTO productDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "deleteImageNames", required = false) List<String> deleteImageNames) {
        try {
            productService.updateProduct(id, productDTO, images);
            return ResponseEntity.ok("제품 수정이 완료되었습니다.");
        } catch (Exception e) {
            log.error("제품 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제품 수정 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "제품 삭제 (관리자)", description = "제품 논리 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProducts(id);
            return ResponseEntity.ok("제품이 삭제되었습니다.");
        } catch (Exception e) {
            log.error("제품 삭제 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제품 삭제 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "제품 복구 (관리자)", description = "삭제된 제품 복구")
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreProduct(@PathVariable Long id) {
        try {
            Product restoredProduct = productService.restoreProduct(id);
            return ResponseEntity.ok("제품이 복구되었습니다.");
        } catch (Exception e) {
            log.error("제품 복구 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("제품 복구 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "제품 통계 (관리자)", description = "제품 통계 정보 조회")
    @GetMapping("/stats")
    public ResponseEntity<?> getProductStats() {
        try {
            long totalProducts = productService.getTotalProductCount();
            long activeProducts = productService.getActiveProductCount();
            long deletedProducts = productService.getDeletedProductCount();
            long seaProducts = productService.getProductCountByMainType(MainType.SEA);
            long freshwaterProducts = productService.getProductCountByMainType(MainType.FRESHWATER);

            return ResponseEntity.ok(Map.of(
                "totalProducts", totalProducts,
                "activeProducts", activeProducts,
                "deletedProducts", deletedProducts,
                "seaProducts", seaProducts,
                "freshwaterProducts", freshwaterProducts
            ));
        } catch (Exception e) {
            log.error("제품 통계 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("통계 조회 실패");
        }
    }

    @Operation(summary = "지역별 제품 목록 (관리자)", description = "지역별 제품 조회")
    @GetMapping("/region/{region}")
    public ResponseEntity<Page<ProductDTO>> getProductsByRegion(
            @PathVariable ProdRegion region,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("prodId").descending());
        Page<ProductDTO> products = productService.getProductsByRegion(region, pageable);
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "타입별 제품 목록 (관리자)", description = "바다/민물 타입별 제품 조회")
    @GetMapping("/type/{mainType}")
    public ResponseEntity<Page<ProductDTO>> getProductsByMainType(
            @PathVariable MainType mainType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("prodId").descending());
        Page<ProductDTO> products = productService.getProductsByMainType(mainType, pageable);
        return ResponseEntity.ok(products);
    }

} 