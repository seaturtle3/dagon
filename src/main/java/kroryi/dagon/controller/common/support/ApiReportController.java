package kroryi.dagon.controller.common.support;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.enums.TargetType;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.service.support.ReportService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "신고 관리 API (사용자 + 관리자)")
public class ApiReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;
    private final ReportRepository reportRepository;

    // 신고 목록 조회 (페이징 및 검색)
    @Operation(summary = "신고 목록조회", description = "사용자 신고 접수 API")
    @GetMapping
    public ResponseEntity<Page<ReportDTO>> getReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String uname,
            @RequestParam(required = false) String reportedName) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReportDTO> reports = reportService.getReports(uname, reportedName, pageable);
        return ResponseEntity.ok(reports);
    }

    // 기존 신고 접수 API (사용자 직접 신고)
    @PostMapping("/create")
    @Operation(summary = "신고 접수", description = "사용자 신고 접수 API")
    public ResponseEntity<?> createReport(@RequestBody ReportRequestDTO reportRequestDTO,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Authorization 헤더에서 Bearer token 추출
            String token = authorizationHeader.substring(7);  // "Bearer " 제거
            Long uno = jwtUtil.getUnoFromToken(token);  // uno 값 추출

            if (uno == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "유효하지 않은 토큰입니다."));
            }

            // 신고 접수 서비스 호출
            reportService.createReport(uno, reportRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "신고가 접수되었습니다."));

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "JWT 처리 중 오류가 발생했습니다."));
        } catch (IllegalArgumentException e) {
            // 중복 신고 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }

    // 조행기 신고 API
    @PostMapping("/fishing-post/{fdId}")
    @Operation(summary = "조행기 신고", description = "조행기 신고 API")
    public ResponseEntity<?> reportFishingPost(@PathVariable Long fdId,
                                              @RequestBody String reason,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        return createTargetReport(TargetType.FISHING_POST, fdId, reason, authorizationHeader);
    }

    // 조황정보 신고 API
    @PostMapping("/fishing-report/{frId}")
    @Operation(summary = "조황정보 신고", description = "조황정보 신고 API")
    public ResponseEntity<?> reportFishingReport(@PathVariable Long frId,
                                                @RequestBody String reason,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        return createTargetReport(TargetType.FISHING_REPORT, frId, reason, authorizationHeader);
    }

    // 상품 신고 API
    @PostMapping("/product/{prodId}")
    @Operation(summary = "상품 신고", description = "상품 신고 API")
    public ResponseEntity<?> reportProduct(@PathVariable Long prodId,
                                          @RequestBody String reason,
                                          @RequestHeader("Authorization") String authorizationHeader) {
        return createTargetReport(TargetType.PRODUCT, prodId, reason, authorizationHeader);
    }

    // 조행기 댓글 신고 API
    @PostMapping("/fishing-post-comment/{commentId}")
    @Operation(summary = "조행기 댓글 신고", description = "조행기 댓글 신고 API")
    public ResponseEntity<?> reportFishingPostComment(@PathVariable Long commentId,
                                                     @RequestBody String reason,
                                                     @RequestHeader("Authorization") String authorizationHeader) {
        return createTargetReport(TargetType.COMMENT_FISHING_POST, commentId, reason, authorizationHeader);
    }

    // 조황정보 댓글 신고 API
    @PostMapping("/fishing-report-comment/{commentId}")
    @Operation(summary = "조황정보 댓글 신고", description = "조황정보 댓글 신고 API")
    public ResponseEntity<?> reportFishingReportComment(@PathVariable Long commentId,
                                                       @RequestBody String reason,
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        return createTargetReport(TargetType.COMMENT_FISHING_REPORT, commentId, reason, authorizationHeader);
    }

    // 공통 신고 처리 메서드
    private ResponseEntity<?> createTargetReport(TargetType targetType, Long targetId, String reason, String authorizationHeader) {
        try {
            // Authorization 헤더에서 Bearer token 추출
            String token = authorizationHeader.substring(7);  // "Bearer " 제거
            Long uno = jwtUtil.getUnoFromToken(token);  // uno 값 추출

            if (uno == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "유효하지 않은 토큰입니다."));
            }

            // ReportRequestDTO 생성
            ReportRequestDTO reportRequestDTO = new ReportRequestDTO();
            reportRequestDTO.setTargetType(targetType);
            reportRequestDTO.setTargetId(targetId);
            reportRequestDTO.setReason(reason);

            // 신고 접수 서비스 호출
            reportService.createReport(uno, reportRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "신고가 접수되었습니다."));

        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "JWT 처리 중 오류가 발생했습니다."));
        } catch (IllegalArgumentException e) {
            // 중복 신고 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "서버 오류: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "신고 상세조회", description = "신고 ID로 상세 정보를 조회합니다.")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
        Optional<ReportDTO> report = reportService.getReportById(id);  // service에서 DTO 반환하도록 처리

        if (report.isPresent()) {
            return ResponseEntity.ok(report.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 신고 내역을 찾을 수 없습니다.");
        }
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        if (!reportRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 신고 내역을 찾을 수 없습니다.");
        }

        reportRepository.deleteById(id);
        return ResponseEntity.ok("신고 내역이 삭제되었습니다.");
    }
}
