package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FishingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingReportRepository extends JpaRepository<FishingReport, Long> {

    @Query("SELECT fr FROM FishingReport fr LEFT JOIN FETCH fr.comments")
    List<FishingReport> findAllWithComments();

}
