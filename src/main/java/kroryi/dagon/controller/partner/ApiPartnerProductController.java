package kroryi.dagon.controller.partner;

import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.service.product.ProductService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partner/product")
public class ApiPartnerProductController {

    private final ProductService productService;
    private final JwtUtil jwtProvider;
    private final ProductRepository productRepository;
    private final FishingReportRepository fishingReportRepository;

    @GetMapping("/my")
    public List<ProductDTO> getMyProducts(@RequestHeader("Authorization") String token) {
        String uno = String.valueOf(jwtProvider.getUnoFromToken(token));
        return productService.getProductsByPartnerUno(uno);
    }


    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long prodId,
                                              @RequestHeader("Authorization") String token)
            throws ChangeSetPersister.NotFoundException {

        String rawToken = token.replace("Bearer ", "");
        Long unoFromToken = jwtProvider.getUnoFromToken(rawToken);
        boolean isAdmin = jwtProvider.isAdmin(rawToken);

        Product product = productService.getProductEntityById(prodId);

        // 권한 확인: 관리자거나, 본인 상품일 경우만 허용
        if (!isAdmin && !product.getPartner().getUno().equals(unoFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        // 엔티티를 DTO로 변환
        ProductDTO productDTO = ProductDTO.fromEntity(product);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/my-products")
    public ResponseEntity<List<ProductDTO>> getMyProducts(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);  // 헤더에서 토큰 꺼내기 (이 메서드 직접 구현 필요)
        if (token == null || !jwtProvider.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long uno = jwtProvider.getUnoFromToken(token);
        List<ProductDTO> products = productService.getProductsByPartnerUno(uno);
        return ResponseEntity.ok(products);
    }




    @PutMapping("/{prodId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long prodId,
                                           @RequestBody ProductDTO productDTO,
                                           @RequestHeader("Authorization") String token) throws ChangeSetPersister.NotFoundException {
        String rawToken = token.replace("Bearer ", "");
        Long unoFromToken = jwtProvider.getUnoFromToken(rawToken);
        boolean isAdmin = jwtProvider.isAdmin(rawToken);

        Product product = productService.getProductEntityById(prodId);
        if (!isAdmin && !product.getPartner().getUno().equals(unoFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }

        productService.updateProducts(prodId, productDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{prodId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long prodId,
                                              @RequestHeader("Authorization") String token)
            throws AccessDeniedException, ChangeSetPersister.NotFoundException {

        String rawToken = token.replace("Bearer ", "");
        Long unoFromToken = jwtProvider.getUnoFromToken(rawToken);
        boolean isAdmin = jwtProvider.isAdmin(rawToken); // 관리자 여부 판별

        // 상품 엔티티 직접 조회 (삭제 대상 상품)
        Product product = productService.getProductEntityById(prodId);

        // 관리자 아니고, 소유자도 아니면 권한 예외
        if (!isAdmin && !product.getPartner().getUno().equals(unoFromToken)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 논리 삭제 처리
        productService.deleteProducts(prodId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestBody ProductDTO productDTO,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // "Bearer TOKEN" 형식이니까 TOKEN만 추출
            String token = authorizationHeader.replace("Bearer ", "");
            Long uno = jwtProvider.getUnoFromToken(token);

            // 서비스에 uno와 DTO 넘기기
            productService.createProduct(productDTO, uno);

            return ResponseEntity.ok().body("상품 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("상품 등록 실패: " + e.getMessage());
        }
    }









}
