package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FishingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FishingDiaryRepository extends JpaRepository<FishingDiary, Long> {

    @Query("SELECT fr FROM FishingReport fr LEFT JOIN FETCH fr.comments")
    List<FishingDiary> findAllWithComments();

    List<FishingDiary> findByProductProdId(Long prodId);

}
