package kroryi.dagon.repository.board;

import kroryi.dagon.entity.fishingCenter.FishingReportImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingReportImageRepository extends JpaRepository<FishingReportImage, Long> {

    // 특정 조황 ID로 이미지 조회
    List<FishingReportImage> findByFishingReport_FrId(Long frId);

    // 특정 조황 ID에서 썸네일 이미지 조회
    FishingReportImage findFirstByFishingReport_FrIdAndIsThumbnailTrue(Long frId);

}
