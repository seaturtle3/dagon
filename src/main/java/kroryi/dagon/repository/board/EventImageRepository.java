package kroryi.dagon.repository.board;

import kroryi.dagon.entity.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImageRepository extends JpaRepository<EventImage, Long> {
    // 필요시 추가 메서드 정의
    java.util.List<EventImage> findByEvent_EventId(Long eventId);
    java.util.List<EventImage> findByEvent_EventIdOrderByOrderIndex(Long eventId);
} 