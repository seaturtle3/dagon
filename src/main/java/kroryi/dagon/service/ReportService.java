package kroryi.dagon.service;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.NotificationDTO;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.SenderType;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final UserService userService;

    public Page<ReportDTO> getReports(String nickname, Pageable pageable) {
        Page<Report> reports;
        if (nickname != null && !nickname.isEmpty()) {
            reports = reportRepository.findByReportedUname(nickname, pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }

        return reports.map(ReportDTO::new);  // DTO 변환
    }

    @Transactional
    public void createReport(Long uno, ReportRequestDTO reportRequestDTO) {
        try {
            // 신고한 유저 정보
            User reporter = (User) userRepository.findByUno(uno)
                    .orElseThrow(() -> new IllegalArgumentException("신고한 유저를 찾을 수 없습니다."));

            // 신고당한 유저 정보
            User reported = userRepository.findByUno(Long.valueOf(reportRequestDTO.getReportedUid()))
                    .orElseThrow(() -> new IllegalArgumentException("신고당한 유저를 찾을 수 없습니다."));

            // 신고 내용 저장
            Report report = Report.builder()
                    .reporter(reporter)
                    .reported(reported)
                    .reason(reportRequestDTO.getReason())
                    .build();

            reportRepository.save(report);

            NotificationDTO notification = new NotificationDTO();
            notification.setReceiverId(null); // 전체 관리자에게 전송
            notification.setSenderId(reporter.getUno());  // 신고한 사람
            notification.setSenderType(SenderType.SYSTEM);  // 시스템 알림
            notification.setType("REPORT");               // 알림 종류
            notification.setTitle("신고 접수 알림");
            notification.setContent(
                    "신고자: " + reporter.getUname() + "\n" +
                            "피신고자: " + reported.getUname() + "\n" +
                            "사유: " + reportRequestDTO.getReason()
            );

            notificationService.sendAdminNotification(notification);
        } catch (Exception e) {
            // 오류 처리 시 JSON 형식으로 반환
            System.out.println("신고 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();  // 스택 트레이스를 출력
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "신고 처리 중 오류가 발생했습니다.", e);
        }
    }
}