package kroryi.dagon.service.pages.admin;

import kroryi.dagon.DTO.DashboardReservationStatsDTO;
import kroryi.dagon.DTO.ReservationCountDTO;
import kroryi.dagon.entity.*;
import kroryi.dagon.enums.ApplicationStatus;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {


    private final String SERVICE_KEY = "qDtp1sb5Rs+div3KJcE+c6UL+5AGUuEwjG5XJueXHXXlnfRsCnxRgYzQIDNnjl1NvpS/KqNRt5lAaRpCidPwSw==";
    private final AdminRepository adminRepository; // 어드민 전용 Repository
    private final UserRepository userRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;
    private final ReservationRepository reservationRepository;
    private final InquiryRepository inquiryRepository;
    private final ReportRepository reportRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAid(username)
                .orElseThrow(() -> new UsernameNotFoundException("관리자를 찾을 수 없습니다: " + username));

        return new org.springframework.security.core.userdetails.User(
                admin.getAid(),
                admin.getApw(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    public long getTodayUserCount() {
        return userRepository.countUsersRegisteredToday();
    }

    public long getInactiveUserCount() {
        return userRepository.countInactiveUsers();
    }

    public long getReportedUserCount() {
        return userRepository.countReportedUsers();
    }

    public long getRecentLoginUserCount() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        return userRepository.countRecentlyLoggedInUsers(weekAgo);
    }


    // 총 예약 수 조회
    public long getTotalReservationCount() {
        return seaFreshwaterFishingRepository.count();
    }

    // 일별 예약 수 조회 (최근 7일)
    public List<Map<String, Object>> getDailyReservationCount(LocalDateTime startDate, LocalDateTime endDate) {
        List<Map<String, Object>> dailyCounts = new ArrayList<>();
        
        // 시작일부터 종료일까지 각 날짜별 예약 수 조회
        for (LocalDateTime date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime nextDate = date.plusDays(1);
            
            Map<String, Object> dailyCount = new HashMap<>();
            dailyCount.put("date", date.toLocalDate());
            
            // 해당 날짜의 예약 수 조회 (fishingAt 기준)
            long count = reservationRepository.countByFishingAtBetween(
                date.withHour(0).withMinute(0).withSecond(0),
                nextDate.withHour(0).withMinute(0).withSecond(0)
            );
            
            dailyCount.put("count", count);
            dailyCounts.add(dailyCount);
        }
        
        return dailyCounts;
    }

    // 가장 많이 예약된 파트너 TOP3 조회
    public List<Map<String, Object>> getTop3Partners() {
        List<Object[]> result = seaFreshwaterFishingRepository.findTop3PartnersByReservationCount(PageRequest.of(0, 3));
        List<Map<String, Object>> topPartners = new ArrayList<>();

        for (Object[] row : result) {
            Map<String, Object> map = new HashMap<>();
            map.put("partnerId", row[0]);
            map.put("partnerName", row[1]);
            map.put("reservationCount", row[2]);
            topPartners.add(map);
        }
        return topPartners;
    }


    public long countPendingApplications() {
        return partnerApplicationRepository.countBypStatus(ApplicationStatus.PENDING);
    }

    public long countUsersByRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            return userRepository.countByRole(userRole);
        } catch (IllegalArgumentException e) {
            // 존재하지 않는 역할인 경우 처리
            return 0;
        }
    }


    public ReservationCountDTO getReservationCounts() {
        LocalDateTime now = LocalDateTime.now();

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        long futureCount = seaFreshwaterFishingRepository.countFutureReservations(now);
        long todayCount = seaFreshwaterFishingRepository.countTodayReservations(startOfDay, endOfDay);


        System.out.println("✅ 미래 예약 수: " + futureCount);
        System.out.println("✅ 오늘 예약 수: " + todayCount);

        return new ReservationCountDTO(futureCount, todayCount);


    }

    public DashboardReservationStatsDTO getReservationStats() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusNanos(1);
        LocalDateTime now = LocalDateTime.now();

        long total = seaFreshwaterFishingRepository.count();

        long todayCount = seaFreshwaterFishingRepository.countTodayReservations(startOfToday, endOfToday);

        // 오늘 이후 예약은 fishingAt이 오늘 끝난 시간 이후
        long upcoming = seaFreshwaterFishingRepository.countFutureReservations(endOfToday.plusNanos(1));

        return new DashboardReservationStatsDTO(total, todayCount, upcoming);
    }

    public Map<String, Long> getReservationStatistics() {
        Map<String, Long> counts = new HashMap<>();

        // 총 예약 수
        long totalReservations = seaFreshwaterFishingRepository.count();

        // 오늘 예약 수 (fishingAt이 오늘인 예약)
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);
        long todayReservations = seaFreshwaterFishingRepository.countByFishingAtBetween(today, tomorrow);

        // 미래 예약 수 (fishingAt이 오늘 이후인 예약)
        long futureReservations = seaFreshwaterFishingRepository.countByFishingAtAfter(today);

        counts.put("totalReservations", totalReservations);
        counts.put("todayReservations", todayReservations);
        counts.put("futureReservations", futureReservations);

        return counts;
    }

    public List<Map<String, Object>> getRecentActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();

        // 최근 예약 조회
        List<Reservation> recentReservations = reservationRepository.findTop10ByOrderByCreatedAtDesc();
        for (Reservation reservation : recentReservations) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "RESERVATION");
            activity.put("id", reservation.getReservationId());
            activity.put("createdAt", reservation.getCreatedAt());
            activity.put("title", "새로운 예약이 생성되었습니다");
            activity.put("details", String.format("%s님이 %s 상품을 예약했습니다",
                reservation.getUser().getUname(),
                reservation.getProduct().getProdName()));
            activities.add(activity);
        }

        // 최근 문의 조회
        List<Inquiry> recentInquiries = inquiryRepository.findTop10ByOrderByCreatedAtDesc();
        for (Inquiry inquiry : recentInquiries) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "INQUIRY");
            activity.put("id", inquiry.getInquiryType());
            activity.put("createdAt", inquiry.getCreatedAt());
            activity.put("title", "새로운 문의가 등록되었습니다");
            activity.put("details", String.format("%s님이 %s 상품에 문의를 남겼습니다",
                inquiry.getUser().getUname(),
                inquiry.getUser().getUname()));
            activities.add(activity);
        }

        // 최근 회원가입 조회
        List<User> recentUsers = userRepository.findTop10ByOrderByCreatedAtDesc();
        for (User user : recentUsers) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "USER");
            activity.put("id", user.getUno());
            activity.put("createdAt", user.getCreatedAt());
            activity.put("title", "새로운 회원이 가입했습니다");
            activity.put("details", String.format("%s님이 회원가입했습니다", user.getUname()));
            activities.add(activity);
        }

        // 최근 신고 조회
        List<Report> recentReports = reportRepository.findTop10ByOrderByCreatedAtDesc();
        for (Report report : recentReports) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("type", "REPORT");
            activity.put("id", report.getId());
            activity.put("createdAt", report.getCreatedAt());
            activity.put("title", "새로운 신고가 접수되었습니다");
            activity.put("details", String.format("%s님이 %s을(를) 신고했습니다",
                report.getReporter().getUname(),
                report.getReported()));
            activities.add(activity);
        }

        // 생성일시 기준으로 정렬
        activities.sort((a, b) -> ((LocalDateTime) b.get("createdAt"))
            .compareTo((LocalDateTime) a.get("createdAt")));

        // 상위 20개만 반환
        return activities.stream()
            .limit(20)
            .collect(Collectors.toList());
    }

}
