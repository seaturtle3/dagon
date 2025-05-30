package kroryi.dagon.service;


import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerFishingReportService {

    private final FishingReportRepository fishingReportRepository;
    private final FileStorageService fileStorageService;

    public FishingReportDTO getMyReport(Long frId, Long uno) throws AccessDeniedException {
        FishingReport report = fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황 정보를 찾을 수 없습니다."));

        if (!report.getUser().getUno().equals(uno)) {
            throw new AccessDeniedException("해당 조황정보에 접근할 수 없습니다.");
        }

        return new FishingReportDTO(report);
    }

    @Transactional
    public FishingReportDTO updateMyReportWithFile(Long frId, Long uno, FishingReportDTO dto, MultipartFile thumbnailFile) throws AccessDeniedException {
        FishingReport report = fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황 정보를 찾을 수 없습니다."));

        if (!report.getUser().getUno().equals(uno)) {
            throw new AccessDeniedException("수정 권한 없음");
        }

        report.setTitle(dto.getTitle());
        report.setContent(dto.getContent());
        report.setFishingAt(dto.getFishingAt());

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String savedPath = fileStorageService.store(thumbnailFile);
            report.setThumbnailUrl(savedPath);
        } else {
            report.setThumbnailUrl(dto.getThumbnailUrl()); // 기존 url 유지
        }

        report.setModifyAt(LocalDateTime.now());

        return new FishingReportDTO(report);
    }


    @Transactional
    public void deleteMyReport(Long frId, Long uno) throws AccessDeniedException {
        FishingReport report = fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황 정보를 찾을 수 없습니다."));

        if (!report.getUser().getUno().equals(uno)) {
            throw new AccessDeniedException("해당 조황정보 삭제 권한이 없습니다.");
        }

        fishingReportRepository.delete(report);
    }

    public List<FishingReportDTO> getMyReports(Long uno) {
        List<FishingReport> reports = fishingReportRepository.findByUserUno(uno);
        return reports.stream()
                .map(report -> new FishingReportDTO(report))
                .collect(Collectors.toList());
    }
}
