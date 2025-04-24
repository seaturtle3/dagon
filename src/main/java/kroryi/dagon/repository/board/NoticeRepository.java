package kroryi.dagon.repository.board;

import kroryi.dagon.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("""
    SELECT n FROM Notice n
    WHERE (:type = 'title' AND n.title LIKE %:keyword%)
       OR (:type = 'content' AND n.content LIKE %:keyword%)
       OR (:type = 'title+content' AND (n.title LIKE %:keyword% OR n.content LIKE %:keyword%))
    ORDER BY n.isTop DESC, n.createdAt DESC
""")
    Page<Notice> searchByKeyword(@Param("keyword") String keyword,
                                 @Param("type") String type,
                                 Pageable pageable);

    Page<Notice> findAllByOrderByIsTopDescCreatedAtDesc(Pageable pageable);
}
