package kroryi.dagon.controller.partner.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.board.FishingReportDiary.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportCreateDTO;
import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.community.fishingReportDiary.ApiFishingReportService;
import kroryi.dagon.security.JwtTokenProvider;
import kroryi.dagon.service.image.FileStorageService;
import kroryi.dagon.service.product.ProductService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingReport", description = "조황정보 API (파트너)")
@RequestMapping("/api/fishing-report")
public class ApiFishingReportController {

    private final ApiFishingReportService apiFishingReportService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PartnerFishingReportService partnerFishingReportService;
    private final UserService userService;
    private final ProductService productService;
    private final FileStorageService fileStorageService;
    private final JwtUtil jwtUtil;



    @Operation(summary = "조황정보 생성")
    @PostMapping("/create")
    public ApiFishingReportDTO createFishingReport(
            @RequestHeader("Authorization") String token,
            @RequestBody ApiFishingReportDTO apiFishingReportDTO) {
        String bearerToken = token.substring(7); // "Bearer " 제거
        Long userUno = jwtTokenProvider.getUserUnoFromToken(bearerToken);
        return apiFishingReportService.createFishingReport(apiFishingReportDTO, userUno);
    }

    @Operation(summary = "조황정보 전체 조회 (페이징)")
    @GetMapping("/get-all")
    public Page<ApiFishingReportDTO> getAllFishingReports(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "frId") String sortBy,
                                                          @RequestParam(defaultValue = "desc") String direction)
    {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return apiFishingReportService.getAllFishingReports(pageable);
    }

    @Operation(summary = "조황정보 ID 조회")
    @GetMapping("/get/{id}")
    public ApiFishingReportDTO getFishingReport(@PathVariable Long id) {
        return apiFishingReportService.getFishingReportById(id);
    }

    @Operation(summary = "조황정보 수정")
    @PutMapping("/update/{id}")
    public Long updateFishingReport(@PathVariable("id") Long frId,
                                    @RequestBody ApiFishingReportDTO apiFishingReportDTO) {
        return apiFishingReportService.updateFishingReport(frId, apiFishingReportDTO);
    }

    @Operation(summary = "조황정보 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingReport(@PathVariable Long id) {
        apiFishingReportService.deleteFishingReport(id);
    }

    //====================================================================================================
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
