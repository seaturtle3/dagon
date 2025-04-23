package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    // 표시 순서 기준 정렬된 전체 리스트 (활성 여부 포함) (관리자페이지용)
    Page<FAQ> findAllByOrderByDisplayOrderAsc(Pageable pageable);

    // 활성화된 FAQ만 가져오기 (사용자페이지용)
    Page<FAQ> findByIsActiveTrueOrderByDisplayOrderAsc(Pageable pageable);


    @Query("""
        SELECT f FROM FAQ f
        WHERE (:type = 'question' AND f.question LIKE %:keyword%)
           OR (:type = 'answer' AND f.answer LIKE %:keyword%)
           OR (:type = 'question+answer' AND (f.question LIKE %:keyword% OR f.answer LIKE %:keyword%))
        ORDER BY f.displayOrder ASC
    """)
    Page<FAQ> searchByKeyword(@Param("keyword") String keyword,
                              @Param("type") String type,
                              Pageable pageable);

    @Query("""
        SELECT f FROM FAQ f
        WHERE f.isActive = true AND (
              (:type = 'question' AND f.question LIKE %:keyword%)
           OR (:type = 'answer' AND f.answer LIKE %:keyword%)
           OR (:type = 'question+answer' AND (f.question LIKE %:keyword% OR f.answer LIKE %:keyword%))
        )
        ORDER BY f.displayOrder ASC
    """)
    Page<FAQ> searchByKeywordAndActive(@Param("keyword") String keyword,
                                       @Param("type") String type,
                                       Pageable pageable);
}
