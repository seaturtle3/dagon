package kroryi.dagon.controller.base.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.FishingCenter.ApiFishingReportDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportCreateDTO;
import kroryi.dagon.DTO.board.FishingCenter.FishingReportDTO;
import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.entity.fishingCenter.TempImage;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import kroryi.dagon.service.PartnerFishingReportService;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.service.community.fishingCenter.ApiFishingReportService;
import kroryi.dagon.service.community.fishingCenter.FishingReportImageService;
import kroryi.dagon.service.community.fishingCenter.TempImageService;
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
import java.util.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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
    private final TempImageService tempImageService;
    private final FishingReportImageService fishingReportImageService;

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
            @RequestPart("dto") ApiFishingReportDTO apiFishingReportDTO
    ) {
        Long userUno = getCurrentUserUno();
        // 1. 조황정보 저장 (reportId 생성)
        ApiFishingReportDTO savedReport = apiFishingReportService.createFishingReport(apiFishingReportDTO, userUno, null);

        // 2. content에서 temp 이미지 id 추출
        Set<Long> tempImageIds = extractTempImageIds(apiFishingReportDTO.getContent());
        List<Long> realImageIds = new ArrayList<>();

        // 3. temp_image에서 이미지 가져와서 fishing_report_image에 저장
        for (Long tempId : tempImageIds) {
            TempImage temp = tempImageService.findById(tempId);
            fishingReportImageService.save(temp.getData(), temp.getContentType(), savedReport.getFrId());
            tempImageService.delete(tempId); // 임시 이미지 삭제
        }

        // 4. content의 <img src="/api/images/temp/{tempImageId}">를 <img src="/api/images/{imageId}">로 치환
        String updatedContent = replaceTempSrcWithReal(apiFishingReportDTO.getContent(), tempImageIds, realImageIds);
        savedReport.setContent(updatedContent);
        apiFishingReportService.updateContent(savedReport.getFrId(), updatedContent);

        return savedReport;
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

    @PostMapping("/images/temp-upload")
    public ResponseEntity<Long> tempUploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        // 1. MultipartFile -> byte[]
        byte[] data = file.getBytes();
        String contentType = file.getContentType();
        // 2. temp_image 테이블에 저장 (id 자동생성)
        Long tempImageId = tempImageService.save(data, contentType);
        // 3. tempImageId 반환
        return ResponseEntity.ok(tempImageId);
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        FishingReportImage image = fishingReportImageService
            .getFishingReportImageById(id);
        if (image == null || image.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }
        // FishingReportImage 엔티티에 contentType 필드가 없으므로 기본값 사용
        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(image.getImageData());
    }



    public Set<Long> extractTempImageIds(String html) {
        if (html == null || html.isBlank()) return Set.of();

        Pattern pattern = Pattern.compile("<img[^>]*src=[\"']/api/images/temp/(\\d+)[\"']");
        Matcher matcher = pattern.matcher(html);

        return matcher.results()
                .map(m -> Long.parseLong(m.group(1)))
                .collect(Collectors.toSet());
    }

    public String replaceTempSrcWithReal(String html, Set<Long> tempImageIds, List<Long> realImageIds) {
        if (html == null || html.isBlank() || tempImageIds.isEmpty() || realImageIds.isEmpty()) return html;

        String result = html;
        Iterator<Long> tempIter = tempImageIds.iterator();
        Iterator<Long> realIter = realImageIds.iterator();

        while (tempIter.hasNext() && realIter.hasNext()) {
            Long tempId = tempIter.next();
            Long realId = realIter.next();
            // 정규식으로 정확히 해당 tempId만 치환
            result = result.replaceAll(
                "/api/images/temp/" + tempId,
                "/api/images/" + realId
            );
        }
        return result;
    }
}
