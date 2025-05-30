package kroryi.dagon.controller.legacy.Partner;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.service.PartnerApplicationService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partners")
@Log4j2
public class ApiPartnerApplicationController {

    private final PartnerApplicationService partnerApplicationService;
    private final JwtUtil jwtUtil;



    @Operation(summary = "파트너 등록", description = "파트너 신청 등록 API")
    @PostMapping("/register")
    public ResponseEntity<?> registerPartnerApplication(
            @RequestBody PartnerApplicationDTO dto,
            @RequestHeader("Authorization") String authToken
    ) {
        // 1. 토큰에서 유저 정보 추출
        String token = authToken.replace("Bearer ", "");
        Long uno = jwtUtil.getUnoFromToken(token);
        String uname = jwtUtil.parseToken(token).get("uname", String.class);

        // 2. 유저 정보 설정
        dto.setUno(uno);
        dto.setUname(uname);

        // 3. 서비스로 전달
        partnerApplicationService.register(dto);

        return ResponseEntity.ok("파트너 신청이 완료되었습니다.");
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
}
