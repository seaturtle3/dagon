package kroryi.dagon.controller.partner.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportCreateDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportDTO;
import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.community.fishingCenter.ApiFishingReportService;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "FishingReport", description = "조황정보 API (파트너)")
@RequestMapping("/api/fishing-report")
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
    public ResponseEntity<?> createFishingReportJson(@RequestBody ApiFishingReportDTO apiFishingReportDTO) {
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
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long userUno = getCurrentUserUno();

        if (apiFishingReportDTO.getTitle() == null || apiFishingReportDTO.getContent() == null) {
            throw new IllegalArgumentException("제목 또는 내용이 누락되었습니다.");
        }

        // if (images == null || images.isEmpty()) {
        //     throw new IllegalArgumentException("이미지는 최소 1장 필요합니다.");
        // }

        return apiFishingReportService.createFishingReport(apiFishingReportDTO, userUno, images);
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
    public List<PartnerFishingReportDTO> getMyReports() {
        Long uno = getCurrentUserUno();
        return partnerFishingReportService.getMySimpleReports(uno);
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

            // 이미지 파일 처리
            String savedFileName = null;
            if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                String originalFilename = thumbnailFile.getOriginalFilename();
                String safeFilename = UUID.randomUUID() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");

                Path savePath = Paths.get(uploadDir, safeFilename);
                Files.copy(thumbnailFile.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                savedFileName = safeFilename;
            }

            FishingReport report = new FishingReport();
            report.setTitle(dto.getTitle());
            report.setContent(dto.getContent());
            report.setFishingAt(dto.getFishingAt().atStartOfDay());  // 여기서 LocalDate 타입 받아서 넣음
            report.setThumbnailUrl(savedFileName);
            report.setUser(user);
            report.setProduct(product);

            FishingReport saved = partnerFishingReportService.save(report);

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
}
