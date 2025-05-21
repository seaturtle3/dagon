package kroryi.dagon.controller.common;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.service.ReportService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "신고 관리 API (공용)")
public class ContentReportController {

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

    // 신고 접수 API

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
