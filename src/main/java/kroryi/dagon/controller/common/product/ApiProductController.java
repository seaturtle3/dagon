package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.enums.MainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.service.product.ProductService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 등록/조회/수정/삭제 API")
@RequestMapping("/api/product")
@Log4j2
public class ApiProductController {
    @Value("${app.file.upload-dir}")
    private String uploadDir;

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새로운 상품을 JSON + 파일로 등록")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> addProduct(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) throws IOException {
        log.info("----/create {}", productDTO);

        String savedFileName = null;

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String originalFilename = thumbnailFile.getOriginalFilename();
            String safeFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

            Path savePath = Paths.get(uploadDir, safeFilename);
            Files.copy(thumbnailFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            savedFileName = safeFilename;
        }

        productDTO.setProdThumbnail(savedFileName); // DTO에 썸네일 파일명 설정

        log.info("----2/create {}", productDTO);

        Long savedId = productService.addProduct(productDTO);

        return ResponseEntity.ok(savedId);
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
