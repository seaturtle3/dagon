package kroryi.dagon.service.community.fishingCenter;

import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import kroryi.dagon.repository.board.FishingDiaryImageRepository;
import kroryi.dagon.service.image.ImageContentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FishingDiaryImageContentProvider implements ImageContentProvider {

    private final FishingDiaryImageRepository fishingDiaryImageRepository;

    @Override
    public String getBoardName() {
        return "조황정보 이미지";
    }

    @Override
    public List<String> getAllContents() {
        // 조황정보 이미지의 imageUrl들을 반환
        return fishingDiaryImageRepository.findAll().stream()
                .map(FishingDiaryImage::getImageUrl)
                .collect(Collectors.toList());
    }
} 