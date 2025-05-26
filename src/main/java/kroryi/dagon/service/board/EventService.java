package kroryi.dagon.service.board;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Event;
import kroryi.dagon.enums.EventStatus;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.EventRepository;
import kroryi.dagon.util.ImageFileUtil;
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
    private final ImageFileUtil imageFileUtil;

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
    public Event createEvent(EventRequestDTO dto, String aid){
        Admin admin = adminRepository.findById(aid).orElseThrow();

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setThumbnailUrl(dto.getThumbnailUrl());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setAdmin(admin);
        event.updateEventStatus(LocalDate.now());

        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, EventRequestDTO dto) {
        Event event = eventRepository.findById(id).orElseThrow();

        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setThumbnailUrl(dto.getThumbnailUrl());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setModifyAt(LocalDateTime.now());
        event.updateEventStatus(LocalDate.now());

        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();

        Set<String> imagesToCheck = imageFileUtil.extractImagePaths(event.getContent());
        eventRepository.delete(event);

        List<String> otherUsedImages = eventRepository.findAll().stream()
                .flatMap(e -> imageFileUtil.extractImagePaths(e.getContent()).stream())
                .toList();

        for (String img : imagesToCheck) {
            if (!otherUsedImages.contains(img)) {
                imageFileUtil.deleteImageFromDisk(img);
            }
        }
    }

    @Transactional
    public void increaseViews(Long id) {
        Event event = eventRepository.findById(id).orElseThrow();
        event.setViews(event.getViews() + 1);
    }


    public Page<Event> searchEvents(BoardSearchDTO dto, Pageable pageable) {
        String keyword = dto.getKeyword();
        String type = dto.getType();
        String statusStr = dto.getStatus(); // 상태 문자열 받기

        EventStatus status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            status = EventStatus.valueOf(statusStr.toUpperCase()); // String을 Enum으로 변환
        }

        if (keyword == null || keyword.isBlank()) {
            if (status != null) {
                return eventRepository.findByEventStatus(status, pageable); // 상태로 필터링
            }
            return eventRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable); // 기본 전체 검색
        }

        // 키워드와 상태로 필터링
        return eventRepository.searchByKeywordAndStatus(keyword, type, status, pageable);
    }
}