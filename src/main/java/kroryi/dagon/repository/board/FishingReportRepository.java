package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FishingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishingReportRepository extends JpaRepository<FishingReport, Long> {
}
