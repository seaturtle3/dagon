package kroryi.dagon.repository.board;

import kroryi.dagon.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByOrderByIsTopDescCreatedAtDesc(Pageable pageable);

    @Query("""
        SELECT e FROM Event e
        WHERE (:type = 'title' AND e.title LIKE %:keyword%)
           OR (:type = 'content' AND e.content LIKE %:keyword%)
           OR (:type = 'title+content' AND (e.title LIKE %:keyword% OR e.content LIKE %:keyword%))
        ORDER BY e.isTop DESC, e.createdAt DESC
    """)
    Page<Event> searchByKeyword(@Param("keyword") String keyword,
                                @Param("type") String type,
                                Pageable pageable);
}