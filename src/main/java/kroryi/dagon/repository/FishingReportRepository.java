package kroryi.dagon.repository;


import kroryi.dagon.entity.FishingReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishingReportRepository extends JpaRepository<FishingReport, Long> {
}
