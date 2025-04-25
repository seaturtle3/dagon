package kroryi.dagon.controller.api.auth;

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
@Tag(name = "1-1. AdminDashboard", description = "관리자 대시보드 통계 API")
public class AdminDashboardController {


    private final AdminDashboardService dashboardService;

    @GetMapping("/counts")
    public ResponseEntity<?> getCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("totalUsers", dashboardService.getTotalUsers());
        counts.put("totalApplications", dashboardService.getTotalApplications());
        counts.put("approvedPartners", dashboardService.getApprovedPartners());

        return ResponseEntity.ok(counts);
    }

}
