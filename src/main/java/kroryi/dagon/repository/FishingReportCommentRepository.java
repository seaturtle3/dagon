package kroryi.dagon.repository;

import kroryi.dagon.entity.FishingReportComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingReportCommentRepository extends JpaRepository<FishingReportComment, Long> {
    List<FishingReportComment> findByFishingReport_FrId(Long frId);
}
