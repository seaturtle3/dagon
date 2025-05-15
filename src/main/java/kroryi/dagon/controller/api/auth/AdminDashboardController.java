package kroryi.dagon.controller.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 대시보드 통계 API")
public class AdminDashboardController {


    private final AdminDashboardService dashboardService;

    @GetMapping("/counts")
    @Operation(summary = "회원 숫자, 파트너 숫자 조회 ", description = "회원 숫자,파트너 숫자 조회")
    public ResponseEntity<?> getCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("totalUsers", dashboardService.getTotalUsers());
        counts.put("totalApplications", dashboardService.getTotalApplications());
        counts.put("approvedPartners", dashboardService.getApprovedPartners());

        return ResponseEntity.ok(counts);
    }

}
