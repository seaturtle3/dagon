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
    public void createReport(Long reporterUid, ReportRequestDTO dto) {

        System.out.println("신고자 UID: " + reporterUid);
        System.out.println("피신고자 UID: " + dto.getReportedUid());
        System.out.println("신고 사유: " + dto.getReason());

        User reporter = userRepository.findByUno(reporterUid)
                .orElseThrow(() -> new IllegalArgumentException("신고자 정보를 찾을 수 없습니다."));

        User reported = userRepository.findByUno(Long.valueOf(dto.getReportedUid()))
                .orElseThrow(() -> new IllegalArgumentException("피신고자 정보를 찾을 수 없습니다."));



        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .reason(dto.getReason())
                .build();

        reportRepository.save(report);



    }
}