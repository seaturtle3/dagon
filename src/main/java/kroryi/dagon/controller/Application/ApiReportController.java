package kroryi.dagon.controller.Application;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.service.ReportService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ApiReportController {

    private final ReportService reportService;
    private final JwtUtil jwtUtil;

    // 신고 목록 조회 (페이징 및 검색)
    @GetMapping
    @Operation(summary = "신고 목록조회", description = "사용자 신고 접수 API")
    public ResponseEntity<Page<ReportDTO>> getReports(
            @RequestParam(required = false) String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReportDTO> reports = reportService.getReports(nickname, pageable);
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
}
