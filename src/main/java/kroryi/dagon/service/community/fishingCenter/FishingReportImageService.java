package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.repository.board.FishingReportImageRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FishingReportImageService {
    private final FishingReportImageRepository fishingReportImageRepository;
    private final FishingReportRepository fishingReportRepository;

    @Transactional
    public Long save(byte[] data, String contentType, Long reportId) {
        FishingReport report = fishingReportRepository.findById(reportId).orElseThrow();
        FishingReportImage image = new FishingReportImage();
        image.setImageData(data);
        image.setFishingReport(report);
        // 필요시 contentType 필드 추가
        fishingReportImageRepository.save(image);
        return image.getId();
    }

    public FishingReportImage getFishingReportImageById(Long id) {
        return fishingReportImageRepository.findById(id).orElse(null);
    }
} 