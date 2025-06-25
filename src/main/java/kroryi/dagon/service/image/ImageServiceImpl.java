package kroryi.dagon.service.image;

import kroryi.dagon.controller.common.image.ImageResponseDTO;
import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.entity.fishingCenter.FishingReport;
import kroryi.dagon.repository.board.FishingReportImageRepository;
import kroryi.dagon.repository.board.FishingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final FishingReportImageRepository fishingReportImageRepository;
    private final FishingReportRepository fishingReportRepository;

    @Override
    public Long saveToDatabase(byte[] data, String originalFilename, String contentType, Long reportId) {
        FishingReportImage image = new FishingReportImage();
        image.setImageData(data);
        if (reportId != null) {
            FishingReport report = fishingReportRepository.findById(reportId).orElse(null);
            image.setFishingReport(report);
        }
        fishingReportImageRepository.save(image);
        return image.getId();
    }

    @Override
    public ImageResponseDTO loadFromDatabase(Long id) {
        FishingReportImage image = fishingReportImageRepository.findById(id).orElse(null);
        if (image == null) return null;
        String contentType = null;
        return new ImageResponseDTO(image.getImageData(), contentType);
    }
} 