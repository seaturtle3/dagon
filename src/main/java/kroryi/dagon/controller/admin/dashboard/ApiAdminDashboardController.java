package kroryi.dagon.controller.admin.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationCountDTO;
import kroryi.dagon.service.pages.admin.AdminDashboardService;
import kroryi.dagon.service.pages.admin.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 대시보드 통계 API")
public class ApiAdminDashboardController {


    private final AdminDashboardService dashboardService;
    private final AdminDetailsService adminDetailsService;

    @GetMapping("/counts")
    @Operation(summary = "회원 숫자, 파트너 숫자 조회 ", description = "회원 숫자,파트너 숫자 조회")
    public ResponseEntity<?> getCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("totalUsers", dashboardService.getTotalUsers());
        counts.put("totalApplications", dashboardService.getTotalApplications());
        counts.put("approvedPartners", dashboardService.getApprovedPartners());

        return ResponseEntity.ok(counts);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("todayUserCount", adminDetailsService.getTodayUserCount());
        stats.put("inactiveUserCount", adminDetailsService.getInactiveUserCount());
        stats.put("reportedUserCount", adminDetailsService.getReportedUserCount());
        stats.put("recentLoginUserCount", adminDetailsService.getRecentLoginUserCount());

        return ResponseEntity.ok(stats);
    }



    // 총 예약 수 API
    @GetMapping("/reservation/total")
    public ResponseEntity<Long> getTotalReservationCount() {
        return ResponseEntity.ok(adminDetailsService.getTotalReservationCount());
    }

    // 일별 예약 수 API (최근 7일)
    @GetMapping("/reservation/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyReservationCount() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        return ResponseEntity.ok(adminDetailsService.getDailyReservationCount(oneWeekAgo));
    }

    // 가장 많이 예약된 파트너 TOP3 API
    @GetMapping("/partner/top3")
    public ResponseEntity<List<Map<String, Object>>> getTop3Partners() {
        return ResponseEntity.ok(adminDetailsService.getTop3Partners());
    }

    @GetMapping("/pending/count")
    public ResponseEntity<Long> getPendingPartnerCount() {
        long count = adminDetailsService.countPendingApplications();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserCounts() {
        long userCount = adminDetailsService.countUsersByRole("USER");
        long partnerCount = adminDetailsService.countUsersByRole("PARTNER");

        Map<String, Long> counts = new HashMap<>();
        counts.put("userCount", userCount);
        counts.put("partnerCount", partnerCount);

        return ResponseEntity.ok(counts);
    }

    @GetMapping("/reservations/counts")
    public ReservationCountDTO getReservationCounts() {
        System.out.println("🎯 [예약] /reservation/total 호출됨");
        return adminDetailsService.getReservationCounts();


    }


}