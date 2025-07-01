package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import kroryi.dagon.repository.board.FishingReportImageRepository;
import kroryi.dagon.service.image.ImageContentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FishingReportImageContentProvider implements ImageContentProvider {

    private final FishingReportImageRepository fishingReportImageRepository;

    @Override
    public String getBoardName() {
        return "조황보고 이미지";
    }

    @Override
    public List<String> getAllContents() {
        // 조황보고 이미지의 imageUrl들을 반환
        return fishingReportImageRepository.findAll().stream()
                .map(FishingReportImage::getImageUrl)
                .collect(Collectors.toList());
    }
} 