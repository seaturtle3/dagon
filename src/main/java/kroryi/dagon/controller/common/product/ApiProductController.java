package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 등록/조회/수정/삭제 API")
@RequestMapping("/api/product")
public class ApiProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새로운 상품 등록")
    @PostMapping("/create")
    public Long addProduct(@RequestBody ProductDTO productDTO) {
        return productService.addProduct(productDTO);
    }

    @Operation(summary = "모든 상품 페이징 조회", description = "페이징으로 상품 조회")
    @GetMapping("/get-all")
    public Page<ProductDTO> getAllProducts (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getAllProductsApi(pageable);
    }


    @Operation(summary = "상품 단건 조회", description = "ID로 상품 조회")
    @GetMapping("/get/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "상품 수정", description = "상품 정보 수정")
    @PutMapping("/update/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return productService.updateProduct(id, productDTO);
    }

    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

//  -------------- 프론트 추가(바다/민물 필터) api ----------------

    @Operation(summary = "바다 상품 페이징 조회", description = "mainType이 '바다'인 상품 페이징 조회")
    @GetMapping("/get-all/sea")
    public Page<ProductDTO> getSeaProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getProductsByMainType(MainType.valueOf("SEA"), pageable);
    }

    @Operation(summary = "민물 상품 페이징 조회", description = "mainType이 '민물'인 상품 페이징 조회")
    @GetMapping("/get-all/freshwater")
    public Page<ProductDTO> getFreshwaterProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getProductsByMainType(MainType.valueOf("FRESHWATER"), pageable);
    }


}
