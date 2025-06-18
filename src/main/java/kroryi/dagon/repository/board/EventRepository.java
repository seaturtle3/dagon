package kroryi.dagon.repository.board;

import kroryi.dagon.entity.Event;
import kroryi.dagon.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByOrderByIsTopDescCreatedAtDesc(Pageable pageable);


    Page<Event> findByTitleContainingIgnoreCaseOrContentContaining(String title, String content, Pageable pageable);

    Page<Event> findByTitleContainingIgnoreCase(String title, Pageable pageable);


    Page<Event> findByContentContaining(String content, Pageable pageable);
}