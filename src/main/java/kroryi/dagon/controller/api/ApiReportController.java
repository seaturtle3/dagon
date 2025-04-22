package kroryi.dagon.controller.api;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ApiReportController {

    private final ReportService reportService;
    private  final JwtUtil jwtUtil;

    // 신고 목록 조회 (페이징 및 검색)
    @GetMapping
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
    @Operation(summary = "신고 접수", description = "유저 신고 API")
    public ResponseEntity<String> createReport(@RequestBody ReportRequestDTO reportRequestDTO,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);  // Bearer <token>에서 token만 추출
        String uno = jwtUtil.getUidFromToken(token);  // 토큰에서 uid 추출

        if (uno == null) {
            return new ResponseEntity<>("유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
        }

        try {
            reportService.createReport(Long.valueOf(uno), reportRequestDTO);
            return ResponseEntity.ok("신고가 접수되었습니다.");
        } catch (Exception e) {
            return new ResponseEntity<>("신고 접수 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
