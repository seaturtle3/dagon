package kroryi.dagon.service.support;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.ReportDTO;
import kroryi.dagon.DTO.ReportRequestDTO;
import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.fishingCenter.FishingDiary;
import kroryi.dagon.entity.fishingCenter.FishingDiaryComment;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.entity.fishingCenter.FishingReportComment;
import kroryi.dagon.entity.Product;
import kroryi.dagon.enums.TargetType;
import kroryi.dagon.repository.ReportRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.board.FishingDiaryRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.repository.FishingDiaryCommentRepository;
import kroryi.dagon.repository.FishingReportCommentRepository;
import kroryi.dagon.service.auth.UserService;
import kroryi.dagon.util.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FishingDiaryRepository fishingDiaryRepository;
    private final FishingReportRepository fishingReportRepository;
    private final FishingDiaryCommentRepository fishingDiaryCommentRepository;
    private final FishingReportCommentRepository fishingReportCommentRepository;
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


    public Optional<ReportDTO> getReportById(Long id) {
        return reportRepository.findById(id)
                .map(ReportDTO::new);  // 바로 생성자 사용
    }

    @Transactional
    public void createReport(Long reporterUno, ReportRequestDTO dto) {
        // 신고자 조회 (uno 사용)
        User reporter = userRepository.findByUno(reporterUno)
                .orElseThrow(() -> new UserNotFoundException("신고자 정보를 찾을 수 없습니다."));

        User reported;
        
        // targetType과 targetId가 제공된 경우 새로운 방식 사용
        if (dto.getTargetType() != null && dto.getTargetId() != null) {
            reported = findReportedUserByTarget(dto.getTargetType(), dto.getTargetId());
        } else {
            // 기존 방식 (reportedUid 사용)
            String reportedUid = dto.getReportedUid();
            reported = userRepository.findByUid(reportedUid)
                    .orElseThrow(() -> new UserNotFoundException("피신고자 정보를 찾을 수 없습니다."));
        }

        // 동일인 신고 방지
        if (reporter.getUno().equals(reported.getUno())) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }
        
        // ✅ 중복 신고 방지 (targetType과 targetId도 고려)
        if (dto.getTargetType() != null && dto.getTargetId() != null) {
            if (reportRepository.existsByReporterAndTargetTypeAndTargetId(reporter, dto.getTargetType(), dto.getTargetId())) {
                throw new IllegalArgumentException("이미 신고한 대상입니다.");
            }
        } else {
            if (reportRepository.existsByReporterAndReported(reporter, reported)) {
                throw new IllegalArgumentException("이미 신고한 사용자입니다.");
            }
        }

        // 신고 저장
        Report report = Report.builder()
                .reporter(reporter)
                .reported(reported)
                .targetType(dto.getTargetType())
                .targetId(dto.getTargetId())
                .reason(dto.getReason())
                .build();

        reportRepository.save(report);
    }

    /**
     * 신고 대상 타입과 ID를 통해 피신고자(User)를 찾는 메서드
     */
    private User findReportedUserByTarget(TargetType targetType, Long targetId) {
        return switch (targetType) {
            case PRODUCT -> {
                Product product = productRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
                yield product.getPartner().getUser();
            }
            case FISHING_POST -> {
                FishingDiary fishingDiary = fishingDiaryRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("조행기를 찾을 수 없습니다."));
                yield fishingDiary.getUser();
            }
            case FISHING_REPORT -> {
                FishingReport fishingReport = fishingReportRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("조황정보를 찾을 수 없습니다."));
                yield fishingReport.getUser();
            }
            case COMMENT_FISHING_POST -> {
                FishingDiaryComment comment = fishingDiaryCommentRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("조행기 댓글을 찾을 수 없습니다."));
                yield comment.getUser();
            }
            case COMMENT_FISHING_REPORT -> {
                FishingReportComment comment = fishingReportCommentRepository.findById(targetId)
                        .orElseThrow(() -> new IllegalArgumentException("조황정보 댓글을 찾을 수 없습니다."));
                yield comment.getUser();
            }
        };
    }
}


