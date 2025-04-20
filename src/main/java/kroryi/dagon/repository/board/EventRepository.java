package kroryi.dagon.repository.board;

import kroryi.dagon.entity.Event;
import kroryi.dagon.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long>{
    List<Event> findByEventStatus(EventStatus status);
}
