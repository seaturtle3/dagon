package kroryi.dagon.controller.base.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportCreateDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportDTO;
import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.community.fishingCenter.ApiFishingReportService;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.util.HashMap;
import java.util.Map;
import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingReport", description = "조황정보 API (파트너)")
@RequestMapping("/api/fishing-report")
@Log4j2
public class ApiFishingReportController {

    @Value("${app.file.upload-dir}")
    private String uploadDir;
    private final ApiFishingReportService apiFishingReportService;
    private final PartnerFishingReportService partnerFishingReportService;
    private final UserService userService;
    private final ProductService productService;

    // 현재 인증된 사용자의 uno를 가져오는 헬퍼 메서드
    private Long getCurrentUserUno() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof kroryi.dagon.component.CustomUserDetails) {
            return ((kroryi.dagon.component.CustomUserDetails) authentication.getPrincipal()).getUno();
        }
        throw new RuntimeException("인증된 사용자 정보를 찾을 수 없습니다.");
    }

    @Operation(summary = "조황정보 생성 (JSON)")
    @PostMapping(value = "/create-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFishingReportJson(
            @RequestBody ApiFishingReportDTO apiFishingReportDTO
    ) {
        try {
            Long userUno = getCurrentUserUno();

            if (apiFishingReportDTO.getTitle() == null || apiFishingReportDTO.getContent() == null) {
                return ResponseEntity.badRequest().body("제목 또는 내용이 누락되었습니다.");
            }

            // 이미지 없이 조황정보 생성
            ApiFishingReportDTO createdReport = apiFishingReportService.createFishingReport(apiFishingReportDTO, userUno, null);
            return ResponseEntity.ok(createdReport);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("조황정보 생성 실패: " + e.getMessage());
        }
    }

    @Operation(summary = "조황정보 생성")
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiFishingReportDTO createFishingReport(
            @RequestPart("dto") ApiFishingReportDTO apiFishingReportDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userUno = userDetails.getUno();
        log.info("fishing create -> getUserUno: {}" , userUno);

        if (apiFishingReportDTO.getTitle() == null || apiFishingReportDTO.getContent() == null) {
            throw new IllegalArgumentException("제목 또는 내용이 누락되었습니다.");
        }
        return apiFishingReportService.createFishingReport(apiFishingReportDTO, userUno, images);
    }

    @Operation(summary = "조황정보 전체 조회 (페이징)")
    @GetMapping("/get-all")
    public Page<ApiFishingReportDTO> getAllFishingReports(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "frId") String sortBy,
                                                          @RequestParam(defaultValue = "desc") String direction) {
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

    @Operation(summary = "조황정보 ID 조회")
    @GetMapping("/edit/{id}")
    public ApiFishingReportDTO getFishingReportEdit(@PathVariable Long id) {
        return apiFishingReportService.getFishingReportById(id);
    }

    @Operation(summary = "조황정보 수정")
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long updateFishingReport(
            @PathVariable("id") Long frId,
            @RequestPart("dto") ApiFishingReportDTO apiFishingReportDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        // 이미지 처리 로직
        if (images == null || images.isEmpty()) {
            // 이미지가 없거나 비어있을 때 기본적으로 기존 이미지 유지
            if (apiFishingReportDTO.getKeepExistingImages() == null) {
                apiFishingReportDTO.setKeepExistingImages(true);
            }
        } else {
            // 새 이미지가 있으면 기존 이미지 유지 플래그를 false로 설정
            apiFishingReportDTO.setKeepExistingImages(false);
        }
        
        log.info("Update fishing report - frId: {}, keepExistingImages: {}, images count: {}", 
                frId, apiFishingReportDTO.getKeepExistingImages(), 
                images != null ? images.size() : 0);
        
        return apiFishingReportService.updateFishingReport(frId, apiFishingReportDTO, images);
    }

    @Operation(summary = "조황정보 삭제")
    @DeleteMapping("/delete/{id}")
    public void deleteFishingReport(@PathVariable Long id) {
        apiFishingReportService.deleteFishingReport(id);
    }

    //====================================================================================================
    @GetMapping("/mine")
    public List<ApiFishingReportDTO> getMyReports() {
        Long uno = getCurrentUserUno();
        return partnerFishingReportService.getMySimpleReportsWithImages(uno);
    }

    @GetMapping("/{frId}")
    public FishingReportDTO getMyReport(@PathVariable Long frId) throws AccessDeniedException {
        Long uno = getCurrentUserUno();
        return partnerFishingReportService.getMyReport(frId, uno);
    }

    @PutMapping("/{frId}")
    public ResponseEntity<?> updateMyReport(
            @PathVariable Long frId,
            @RequestPart("dto") FishingReportDTO dto,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) throws AccessDeniedException {

        Long uno = getCurrentUserUno();
        FishingReportDTO updatedReport = partnerFishingReportService.updateMyReportWithFile(frId, uno, dto, thumbnailFile);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{frId}")
    public void deleteMyReport(@PathVariable Long frId) throws AccessDeniedException {
        Long uno = getCurrentUserUno();
        partnerFishingReportService.deleteMyReport(frId, uno);
    }

    @PostMapping("")
    public ResponseEntity<?> createFishingReport(
            @RequestPart("dto") FishingReportCreateDTO dto,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {

        try {
            Long uno = getCurrentUserUno();

            User user = userService.getUserByUno(uno);
            Product product = productService.getProductEntityById(dto.getProdId());

            // 권한 체크: 로그인 사용자와 상품 파트너가 일치하는지
            if (!product.getPartner().getUno().equals(uno)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 상품에 대한 권한이 없습니다.");
            }

            FishingReport report = new FishingReport();
            report.setTitle(dto.getTitle());
            report.setContent(dto.getContent());
            report.setFishingAt(dto.getFishingAt().atStartOfDay());  // 여기서 LocalDate 타입 받아서 넣음
            report.setUser(user);
            report.setProduct(product);

            // 이미지 파일을 리스트로 만들어 전달
            List<MultipartFile> images = null;
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                images = List.of(thumbnailFile);
            }

            FishingReport saved = partnerFishingReportService.saveWithImages(report, images);

            return ResponseEntity.ok(saved.getFrId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("조황 등록 실패: " + e.getMessage());
        }
    }

    public String saveImageWithThumbnail(MultipartFile file, String folderName) {
        try {
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir, folderName, dateFolder);
            Files.createDirectories(uploadPath);

            // 원본 저장
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            // 썸네일 생성 및 저장
            String thumbFileName = "thumb_" + fileName;
            Path thumbPath = uploadPath.resolve(thumbFileName);

            BufferedImage originalImage = ImageIO.read(filePath.toFile());
            Thumbnails.of(originalImage)
                    .size(400, 300) // 원하는 썸네일 크기
                    .toFile(thumbPath.toFile());

            // 원본 이미지 URL 반환 (필요시 썸네일 URL도 함께 반환 가능)
            return "/uploads/" + folderName + "/" + dateFolder + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장/썸네일 생성 실패", e);
        }
    }

    @GetMapping("/{currentId}/prev-next")
    public Map<String, ApiFishingReportDTO> getPrevNextFishingReport(@PathVariable Long currentId) {
        // prev: 현재 id보다 작은 것 중 가장 큰 frId
        // next: 현재 id보다 큰 것 중 가장 작은 frId
        ApiFishingReportDTO prev = null;
        ApiFishingReportDTO next = null;
        FishingReport prevEntity = apiFishingReportService.findPrevById(currentId);
        FishingReport nextEntity = apiFishingReportService.findNextById(currentId);
        if (prevEntity != null) prev = new ApiFishingReportDTO(prevEntity);
        if (nextEntity != null) next = new ApiFishingReportDTO(nextEntity);
        Map<String, ApiFishingReportDTO> result = new HashMap<>();
        result.put("prev", prev);
        result.put("next", next);
        return result;
    }
}
