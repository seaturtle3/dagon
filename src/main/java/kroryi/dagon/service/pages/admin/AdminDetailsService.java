package kroryi.dagon.service.pages.admin;

import kroryi.dagon.DTO.ReservationCountDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.enums.ApplicationStatus;
import kroryi.dagon.enums.UserRole;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.PartnerApplicationRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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

@Service
@RequiredArgsConstructor
public class AdminDetailsService implements UserDetailsService {


    private final String SERVICE_KEY = "qDtp1sb5Rs+div3KJcE+c6UL+5AGUuEwjG5XJueXHXXlnfRsCnxRgYzQIDNnjl1NvpS/KqNRt5lAaRpCidPwSw==";
    private final AdminRepository adminRepository; // 어드민 전용 Repository
    private final UserRepository userRepository;
    private final PartnerApplicationRepository partnerApplicationRepository;
    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;

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
    public List<Map<String, Object>> getDailyReservationCount(LocalDateTime fromDate) {
        List<Object[]> result = seaFreshwaterFishingRepository.countDailyReservations(fromDate);
        List<Map<String, Object>> dailyCounts = new ArrayList<>();

        for (Object[] row : result) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", row[0]);
            map.put("count", row[1]);
            dailyCounts.add(map);
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
}
