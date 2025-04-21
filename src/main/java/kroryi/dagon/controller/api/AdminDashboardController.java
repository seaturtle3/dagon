package kroryi.dagon.controller.api;

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
