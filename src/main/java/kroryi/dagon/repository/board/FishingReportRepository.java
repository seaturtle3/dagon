package kroryi.dagon.repository.board;

import kroryi.dagon.entity.fishingCenter.FishingReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FishingReportRepository extends JpaRepository<FishingReport, Long> {

    @Query("SELECT r FROM FishingReport r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.product " +
            "WHERE r.frId = :frId AND r.user.uno = :uno")
    Optional<FishingReport> findWithUserAndProductByIdAndUno(@Param("frId") Long frId);


    @Query("SELECT fr FROM FishingReport fr LEFT JOIN FETCH fr.comments")
    List<FishingReport> findAllWithComments();

    Page<FishingReport> findByProductProdId(Long prodId, Pageable pageable);


    // 기존: List<FishingReport> findByUser_Uid(String uid);
    List<FishingReport> findByUser_Uno(Long uno);

    List<FishingReport> findByUserUno(Long uno);

    List<FishingReport> findByProduct_ProdId(Long prodId);
    
    // 조황정보 댓글 가져오기
    @Query("SELECT fr FROM FishingReport fr LEFT JOIN FETCH fr.comments WHERE fr.frId = :id")
    Optional<FishingReport> findByIdWithComments(@Param("id") Long id);


    List<FishingReport> findByProduct_Partner_Uno(Long partnerId);
}
