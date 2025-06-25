package kroryi.dagon.repository.board;

import kroryi.dagon.entity.fishingCenter.FishingDiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingDiaryImageRepository extends JpaRepository<FishingDiaryImage, Long> {

    // 특정 조황 ID로 이미지 조회
    List<FishingDiaryImage> findByFishingDiary_FdId(Long fdId);

}
