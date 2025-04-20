package kroryi.dagon.service.board;

import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Event;
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
        return eventRepository.findAll(pageable);
    }

    public Event findById(long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public List<Event> getAllEvents() {
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
}
