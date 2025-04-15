package kroryi.dagon.controller.api;

import jakarta.persistence.EntityNotFoundException;
import kroryi.dagon.DTO.PartnerApplicationDTO;
import kroryi.dagon.compoent.CustomUserDetails;
import kroryi.dagon.service.PartnerApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/partners")
@Log4j2
public class PartnerApplicationApiController {

    private final PartnerApplicationService partnerApplicationService;

    /**
     * 파트너 신청 목록을 페이지네이션과 필터링 기능이 있는 API로 제공
     */
    @GetMapping
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
    public ResponseEntity<Void> rejectApplication(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        try {
            partnerApplicationService.reject(id, reason);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
