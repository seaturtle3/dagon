package kroryi.dagon.controller.admin.dashboard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.DashboardReservationStatsDTO;
import kroryi.dagon.DTO.ReservationCountDTO;
import kroryi.dagon.service.pages.admin.AdminDashboardService;
import kroryi.dagon.service.pages.admin.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 일별 예약 수 API (현재 날짜부터 앞으로 7일)
    @GetMapping("/reservation/daily")
    public ResponseEntity<List<Map<String, Object>>> getDailyReservationCount() {
        // 오늘 날짜의 시작(00:00:00)부터 앞으로 7일
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sevenDaysLater = today.plusDays(6); // 6일을 더하면 7일이 됨 (오늘 포함)
        return ResponseEntity.ok(adminDetailsService.getDailyReservationCount(today, sevenDaysLater));
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
    public ResponseEntity<Map<String, Long>> getReservationCounts() {
        return ResponseEntity.ok(adminDetailsService.getReservationStatistics());
    }

    @GetMapping("/reservations")
    public DashboardReservationStatsDTO getReservationStats() {
        return adminDetailsService.getReservationStats();
    }

    @GetMapping("/activities")
    @Operation(summary = "최근 활동 조회", description = "예약, 문의, 회원가입, 신고 등의 최근 활동을 조회합니다.")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities() {
        return ResponseEntity.ok(adminDetailsService.getRecentActivities());
    }

}