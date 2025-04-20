package kroryi.dagon.service;

import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FishingReportService {

    private final FishingReportRepository fishingReportRepository;

    public FishingReportDTO createFishingReport(FishingReportDTO fishingReportDTO) {
        FishingReport fishingReport = new FishingReport();
        fishingReport.setTitle(fishingReportDTO.getTitle());
        fishingReport.setContent(fishingReportDTO.getContent());
        fishingReport.setThumbnailUrl(fishingReportDTO.getThumbnailUrl());
        fishingReport.setFishingAt(fishingReportDTO.getFishingAt());
        fishingReport.setModifyAt(fishingReportDTO.getModifyAt());
        fishingReport.setViews(fishingReportDTO.getViews());
        fishingReport.setUser(fishingReportDTO.getUser());
        fishingReport.setProduct(fishingReportDTO.getProduct());
        fishingReport.setComments(fishingReportDTO.getComments());

        fishingReport = fishingReportRepository.save(fishingReport);

        return new FishingReportDTO(fishingReport);
    }

    public List<FishingReportDTO> getAllFishingReport() {
        List<FishingReport> fishingReports = fishingReportRepository.findAll();
        return fishingReports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FishingReport getFishingReport(Long frId) {
        return fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황정보를 찾을 수 없습니다."));
    }

    public Long updateFishingReport(Long frId, FishingReportDTO fishingReportDTO) {
        FishingReport fishingReport = fishingReportRepository.findById(frId)
                .orElseThrow(() -> new RuntimeException("조황정보 없음"));

        fishingReport.setTitle(fishingReportDTO.getTitle());
        fishingReport.setContent(fishingReportDTO.getContent());
        fishingReport.setThumbnailUrl(fishingReportDTO.getThumbnailUrl());
        fishingReport.setFishingAt(fishingReportDTO.getFishingAt());
        fishingReport.setModifyAt(fishingReportDTO.getModifyAt());
        fishingReport.setViews(fishingReportDTO.getViews());
        fishingReport.setUser(fishingReportDTO.getUser());
        fishingReport.setProduct(fishingReportDTO.getProduct());
        fishingReport.setComments(fishingReportDTO.getComments());

        fishingReportRepository.save(fishingReport);

        return fishingReport.getFrId();
    }

    public void deleteFishingReport(Long frId) {
        fishingReportRepository.deleteById(frId);
    }


    public FishingReportDTO convertToDTO(FishingReport fishingReport) {
        FishingReportDTO dto = new FishingReportDTO();
        dto.setFrId(fishingReport.getFrId());
        dto.setTitle(fishingReport.getTitle());
        dto.setContent(fishingReport.getContent());
        dto.setThumbnailUrl(fishingReport.getThumbnailUrl());
        dto.setFishingAt(fishingReport.getFishingAt());
        dto.setModifyAt(fishingReport.getModifyAt());
        dto.setViews(fishingReport.getViews());
        dto.setUser(fishingReport.getUser());
        dto.setProduct(fishingReport.getProduct());
        dto.setComments(fishingReport.getComments());

        return dto;
    }

}
