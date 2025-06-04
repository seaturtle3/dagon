package kroryi.dagon.service;


import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;

import kroryi.dagon.DTO.board.PartnerFishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.repository.board.FishingReportRepository;
import kroryi.dagon.service.image.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
        } else if (dto.getThumbnailUrl() != null && !dto.getThumbnailUrl().isBlank()) {
            // 클라이언트가 기존 URL을 명시적으로 보낸 경우
            report.setThumbnailUrl(dto.getThumbnailUrl());
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

    public List<PartnerFishingReportDTO> getMySimpleReports(Long uno) {
        List<FishingReport> reports = fishingReportRepository.findByUserUno(uno);
        return reports.stream()
                .map(PartnerFishingReportDTO::new)
                .collect(Collectors.toList());
    }


    public String uploadThumbnail(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 예: 로컬에 저장 (또는 S3 업로드 등)
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/thumbnails", filename);

        try {
            Files.createDirectories(path.getParent());
            file.transferTo(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("썸네일 업로드 실패", e);
        }

        // 실제 서비스에선 URL 리턴 (예: http://localhost:8080/uploads/...)
        return "/uploads/thumbnails/" + filename;
    }


    public FishingReport save(FishingReport report) {
        return fishingReportRepository.save(report);
    }


}
