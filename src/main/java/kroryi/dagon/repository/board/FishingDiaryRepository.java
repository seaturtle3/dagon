package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FishingDiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FishingDiaryRepository extends JpaRepository<FishingDiary, Long> {

    @Query("SELECT fr FROM FishingDiary fr LEFT JOIN FETCH fr.comments")
    List<FishingDiary> findAllWithComments();

    Page<FishingDiary> findByProductProdId(Long prodId, Pageable pageable);

    // 특정 배 상품ID 조행기 조회
    List<FishingDiary> findByProduct_ProdId(Long prodId);

}
