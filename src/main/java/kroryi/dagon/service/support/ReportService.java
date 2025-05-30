package kroryi.dagon.service.support;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.User;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.util.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private final UserService userService;

    public Page<ReportDTO> getReports(String uname, String reportedName, Pageable pageable) {
        Page<Report> reports;

        if (uname != null && !uname.isEmpty()) {
            reports = reportRepository.findByReporter_UnameContaining(uname, pageable);
        } else if (reportedName != null && !reportedName.isEmpty()) {
            reports = reportRepository.findByReported_UnameContaining(reportedName, pageable);
        } else {
            reports = reportRepository.findAll(pageable);
        }

        return reports.map(ReportDTO::new);
    }

    @Transactional
    public void createReport(Long reporterUno, ReportRequestDTO dto) {
        // 신고자 조회 (uno 사용)
        User reporter = userRepository.findByUno(reporterUno)
                .orElseThrow(() -> new UserNotFoundException("신고자 정보를 찾을 수 없습니다."));

        // 피신고자 조회 (uid 사용)
        String reportedUid = dto.getReportedUid();
        User reported = userRepository.findByUid(reportedUid)
                .orElseThrow(() -> new UserNotFoundException("피신고자 정보를 찾을 수 없습니다."));

        // 동일인 신고 방지
        if (reporter.getUno().equals(reported.getUno())) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }
        // ✅ 중복 신고 방지
        if (reportRepository.existsByReporterAndReported(reporter, reported)) {
            throw new IllegalArgumentException("이미 신고한 사용자입니다.");
        }

        // 신고 저장
        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reason(dto.getReason())
                .build();

        reportRepository.save(report);
    }
}


