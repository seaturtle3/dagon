package kroryi.dagon.controller.Partner;


import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportCreateDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.service.product.ProductService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/fishing-report")
@RequiredArgsConstructor
public class ApiPartnerFishingReportController {

    private final PartnerFishingReportService partnerFishingReportService;
    private final UserService userService;
    private final ProductService productService;
    private final FileStorageService fileStorageService;
    private final JwtUtil jwtUtil;


    @GetMapping("/mine")
    public List<PartnerFishingReportDTO> getMyReports(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        return partnerFishingReportService.getMySimpleReports(uno);
    }


    @GetMapping("/{frId}")
    public FishingReportDTO getMyReport(@PathVariable Long frId, HttpServletRequest request) throws AccessDeniedException {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        return partnerFishingReportService.getMyReport(frId, uno);
    }

    @PutMapping("/{frId}")
    public ResponseEntity<?> updateMyReport(
            @PathVariable Long frId,
            @RequestPart("dto") FishingReportDTO dto,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            HttpServletRequest request) throws AccessDeniedException {

        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);

        FishingReportDTO updatedReport = partnerFishingReportService.updateMyReportWithFile(frId, uno, dto, thumbnailFile);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{frId}")
    public void deleteMyReport(@PathVariable Long frId, HttpServletRequest request) throws AccessDeniedException {
        String token = jwtUtil.resolveToken(request);
        Long uno = jwtUtil.getUnoFromToken(token);
        partnerFishingReportService.deleteMyReport(frId, uno);
    }

    @PostMapping("")
    public ResponseEntity<?> createFishingReport(
            @RequestPart("dto") FishingReportCreateDTO dto,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // 토큰에서 USER NO 추출
            String token = authorizationHeader.replace("Bearer ", "");
            Long uno = jwtUtil.getUnoFromToken(token);

            User user = userService.getUserByUno(uno);
            Product product = productService.getProductEntityById(dto.getProdId());

            // 권한 체크: 로그인 사용자와 상품 파트너가 일치하는지
            if (!product.getPartner().getUno().equals(uno)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 상품에 대한 권한이 없습니다.");
            }

            String thumbnailUrl = null;
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                thumbnailUrl = fileStorageService.store(thumbnailFile); // 파일 저장 후 URL 반환
            }
            FishingReport report = new FishingReport();
            report.setTitle(dto.getTitle());
            report.setContent(dto.getContent());
            report.setFishingAt(dto.getFishingAt().atStartOfDay());  // 여기서 LocalDate 타입 받아서 넣음
            report.setThumbnailUrl(thumbnailUrl);
            report.setUser(user);
            report.setProduct(product);

            FishingReport saved = partnerFishingReportService.save(report);

            return ResponseEntity.ok(saved.getFrId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("조황 등록 실패: " + e.getMessage());
        }
    }
}
