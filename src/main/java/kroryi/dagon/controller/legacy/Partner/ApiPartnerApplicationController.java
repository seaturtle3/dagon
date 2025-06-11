package kroryi.dagon.controller.legacy.Partner;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.DTO.PartnerDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Partner;
import kroryi.dagon.service.approval.PartnerApplicationService;
import kroryi.dagon.service.auth.PartnerService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partners")
@Log4j2
public class ApiPartnerApplicationController {

    private final PartnerApplicationService partnerApplicationService;
    private final JwtUtil jwtUtil;
    private final PartnerService partnerService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerPartnerApplication(
            @RequestPart("data") PartnerApplicationDTO dto,
            @RequestPart("businessLicenseImage") MultipartFile businessLicenseImage,
            @RequestHeader("Authorization") String authToken
    ) {
        try {
            String token = authToken.replace("Bearer ", "");
            Long uno = jwtUtil.getUnoFromToken(token);
            String uname = jwtUtil.parseToken(token).get("uname", String.class);

            dto.setUno(uno);
            dto.setUname(uname);

            String uploadDir = "C:/Users/edu002/IdeaProjects/dagon/uploads";
            String filename = UUID.randomUUID() + "_" + businessLicenseImage.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, filename);
            Files.createDirectories(filePath.getParent());
            businessLicenseImage.transferTo(filePath.toFile());

            dto.setBusinessLicenseImage(filePath.toString());

            partnerApplicationService.register(dto);

            return ResponseEntity.ok("파트너 신청이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("파트너 신청 중 오류 발생: " + e.getMessage());
        }
    }



    /**
     * 파트너 신청 목록을 페이지네이션과 필터링 기능이 있는 API로 제공
     */
    @GetMapping
    @Operation(summary = "페이징 검색 필터링 ", description = "페이징 검색 필터링")
    public ResponseEntity<Page<PartnerApplicationDTO>> getPartnerApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        log.info("getPartnerApplications-- >{}", userDetails);

        Page<PartnerApplicationDTO> applications;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // 검색어와 타입이 있는 경우
            applications = partnerApplicationService.searchApplications(type, keyword, status, PageRequest.of(page, size));
        } else {
            // 필터 없이 모든 신청 조회
            applications = partnerApplicationService.getAllApplications(PageRequest.of(page, size));
        }

        return ResponseEntity.ok(applications);
    }

    /**
     * 특정 파트너 신청 상세 정보 조회 API
     */
    @GetMapping("/{id}")
    @Operation(summary = "파트너 신청 상세 조회 ", description = "파트너 신청 상세 조회")
    public ResponseEntity<PartnerApplicationDTO> getPartnerApplication(@PathVariable Long id) {
        try {
            PartnerApplicationDTO dto = partnerApplicationService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 파트너 신청 승인 API
     */
    @PostMapping("/{id}/approve")
    @Operation(summary = "파트너 신청 승인 ", description = "파트너 신청 승인")
    public ResponseEntity<Void> approveApplication(@PathVariable Long id) {
        try {
            partnerApplicationService.approve(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 파트너 신청 반려 API
     */
    @PostMapping("/{id}/reject")
    @Operation(summary = "파트너 신청 반려 ", description = "파트너 신청 반려")
    public ResponseEntity<Void> rejectApplication(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody // JSON 데이터를 받기 위해 Map 사용
    ) {
        try {
            String reason = requestBody.get("reason"); // JSON에서 reason 값을 추출
            partnerApplicationService.reject(id, reason);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    public Page<PartnerDTO> getPartners(
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Partner> partnersPage = partnerService.searchPartners(searchType, keyword, pageable);
        return partnersPage.map(PartnerDTO::new);
    }
}
