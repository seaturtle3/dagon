package kroryi.dagon.service.community;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Event;
import kroryi.dagon.enums.EventStatus;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AdminRepository adminRepository;

    public Page<Event> findAllPaged(Pageable pageable) {
        return eventRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
    }

    public Event findById(long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event createEvent(EventRequestDTO dto, String aid) {
        Admin admin = adminRepository.findById(aid).orElseThrow();

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setAdmin(admin);

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, EventRequestDTO dto, String aid) {
        Event event = eventRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setModifyAt(LocalDateTime.now());
        event.setAdmin(admin);

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id, String aid) {
        Event event = eventRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        eventRepository.delete(event);
    }

    @Transactional
    public void increaseViews(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setViews(event.getViews() + 1);
    }


    public Page<Event> searchEvents(BoardSearchDTO dto, Pageable pageable) {
        String keyword = dto.getKeyword();
        String type = dto.getType();

        Page<Event> base;

        if (keyword == null || keyword.isBlank()) {
            base = eventRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
        } else {
            if ("title".equalsIgnoreCase(type)) {
                base = eventRepository.findByTitleContainingIgnoreCase(keyword, pageable);
            } else if ("content".equalsIgnoreCase(type)) {
                base = eventRepository.findByContentContaining(keyword, pageable);
            } else {
                base = eventRepository.findByTitleContainingIgnoreCaseOrContentContaining(keyword, keyword, pageable);
            }
        }

        // 🔽 상태 필터링 (자바단 처리)
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            try {
                EventStatus desired = EventStatus.valueOf(dto.getStatus().toUpperCase());
                List<Event> filtered = base.getContent().stream()
                        .filter(e -> e.getEventStatus() == desired)
                        .collect(Collectors.toList());

                return new PageImpl<>(filtered, pageable, filtered.size());
            } catch (IllegalArgumentException e) {
                // 잘못된 status 값 방어
                return Page.empty();
            }
        }

        return base;
    }
}