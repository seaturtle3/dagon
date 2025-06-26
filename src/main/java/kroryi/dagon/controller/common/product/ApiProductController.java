package kroryi.dagon.controller.common.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.service.storage.ProductImageStorageService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;
import kroryi.dagon.DTO.product.ProductDTO;
import kroryi.dagon.service.product.ProductService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "상품 등록/조회/수정/삭제 API")
@RequestMapping("/api/product")
@Log4j2
public class ApiProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;
    private final ProductRepository productRepository;
    private final ProductImageStorageService productImageStorageService;

    @Operation(summary = "상품 등록", description = "토큰 기반 인증 후, 상품 정보를 JSON과 썸네일 파일로 등록합니다.")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductWithFile(
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "thumbnailFiles", required = false) List<MultipartFile> thumbnailFiles,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            Long uno = jwtUtil.getUnoFromToken(token);

            List<String> savedFileNames = new ArrayList<>();
            if (thumbnailFiles != null) {
                for (MultipartFile file : thumbnailFiles) {
                    if (file.isEmpty()) continue;

                    // ⬇️ 서비스 통해 이미지 저장
                    String fileName = productImageStorageService.save(file);

                    savedFileNames.add(fileName);
                }
            }

            productDTO.setProdImageNames(savedFileNames);
            productService.createProductWithImages(productDTO, uno);

            return ResponseEntity.ok("상품 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 등록 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "모든 상품 페이징 조회", description = "페이징으로 상품 조회")
    @GetMapping("/get-all")
    public Page<ProductDTO> getAllProducts(
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
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateProduct(
            @PathVariable Long id,
            @RequestPart("product") ProductDTO productDTO,
            @RequestPart(value = "thumbnailFiles", required = false) List<MultipartFile> thumbnailFiles) {

        return productService.updateProduct(id, productDTO, thumbnailFiles);
    }

    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    //  -------------- 프론트 api (바다/민물 필터) ----------------
    @Operation(summary = "바다 상품 페이징 조회", description = "mainType이 '바다'인 상품 페이징 조회")
    @GetMapping("/get-all/sea")
    public Page<ProductDTO> getSeaProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
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
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.getProductsByMainType(MainType.valueOf("FRESHWATER"), pageable);
    }

    //  -------------- 프론트 api 바다 낚시 상품들 ----------------
    @GetMapping("/get-all/sea/filter")
    public List<ProductDTO> getSeaProductsByFilters(
            @RequestParam(required = false) String subType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String species,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        SubType subTypeEnum = (subType == null || subType.isEmpty()) ? null : SubType.valueOf(subType);
        ProdRegion regionEnum = (region == null || region.isEmpty()) ? null : ProdRegion.valueOf(region);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return productService.getFishingCenterProductsByFilters(regionEnum, subTypeEnum, species, sort);
    }

    @GetMapping("/get-all/freshwater/filter")
    public List<ProductDTO> getFreshwaterProductsByFilters(
            @RequestParam(required = false) String subType,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String species,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        SubType subTypeEnum = (subType == null || subType.isEmpty()) ? null : SubType.valueOf(subType);
        ProdRegion regionEnum = (region == null || region.isEmpty()) ? null : ProdRegion.valueOf(region);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return productService.getFishingCenterProductsByFilters(regionEnum, subTypeEnum, species, sort);
    }

    //  -------------- 프론트 api 바다/민물 상단 필터 ----------------
    @GetMapping("/sea/filter")
    public Map<String, List<String>> getSeaFilterOptions() {
        List<String> regions = Arrays.stream(ProdRegion.values())
                .map(Enum::name)
                .toList();

        List<String> subTypes = Arrays.stream(SubType.values())
                .map(Enum::name)
                .toList();

        List<String> species = productRepository.findAllSeaFishSpecies(); // <-- 여기를 새 메서드로 변경

        Map<String, List<String>> filters = new HashMap<>();
        filters.put("regions", regions);
        filters.put("subTypes", subTypes);
        filters.put("fishSpecies", species);
        return filters;
    }

    @GetMapping("/freshwater/filter")
    public Map<String, List<String>> getFreshwaterFilterOptions() {
        List<String> regions = Arrays.stream(ProdRegion.values())
                .map(Enum::name)
                .toList();

        List<String> subTypes = Arrays.stream(SubType.values())
                .map(Enum::name)
                .toList();

        List<String> species = productRepository.findAllFreshwaterFishSpecies(); // <-- 여기를 새 메서드로 변경

        Map<String, List<String>> filters = new HashMap<>();
        filters.put("regions", regions);
        filters.put("subTypes", subTypes);
        filters.put("fishSpecies", species);
        return filters;
    }

    @Operation(summary = "상품 키워드 검색", description = "검색어(keyword)로 상품을 페이징 조회")
    @GetMapping("/search")
    public Page<ProductDTO> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "prodId") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.searchProductsByKeyword(keyword, pageable);
    }

}
