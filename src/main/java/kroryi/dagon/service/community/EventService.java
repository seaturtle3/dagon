package kroryi.dagon.service.community;
import org.springframework.data.domain.PageImpl;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.Event;
import kroryi.dagon.entity.EventImage;
import kroryi.dagon.enums.EventStatus;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.EventImageRepository;
import kroryi.dagon.repository.board.EventRepository;
import kroryi.dagon.util.ImageFileUtil;
import kroryi.dagon.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import net.coobird.thumbnailator.Thumbnails;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
@Log4j2
public class EventService {

    private final EventRepository eventRepository;
    private final AdminRepository adminRepository;
    private final ImageFileUtil imageFileUtil;
    private final FileStorageUtil fileStorageUtil;
    private final EventImageRepository eventImageRepository;

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
        event.setThumbnailUrl(dto.getThumbnailUrl());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setAdmin(admin);
        Event saved = eventRepository.save(event);

        // 이미지 저장
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            saveEventImages(saved, dto.getImages());
        }
        return saved;
    }

    @Transactional
    public Event updateEvent(Long id, EventRequestDTO dto, String aid) {
        Event event = eventRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        // 기존 이미지 파일 삭제
        List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(id);
        for (EventImage img : existingImages) {
            if (img.getImageUrl() != null) {
                fileStorageUtil.deleteImage(img.getImageUrl());
            }
        }

        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setThumbnailUrl(dto.getThumbnailUrl());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setModifyAt(LocalDateTime.now());
        event.setAdmin(admin);
        Event saved = eventRepository.save(event);

        // 기존 이미지 DB 삭제 후 새 이미지 저장
        eventImageRepository.deleteAll(existingImages);
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            saveEventImages(saved, dto.getImages());
        }
        return saved;
    }

    @Transactional
    public void saveEventImages(Event event, List<MultipartFile> images) {
        int idx = 0;
        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;
            
            try {
                // 1. 파일시스템에 저장
                String imageUrl = fileStorageUtil.saveImage(file, "event");
                
                // 2. 썸네일 생성 (파일 바이트 배열을 안전하게 처리)
                byte[] fileBytes = file.getBytes();
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileBytes));
                
                if (originalImage == null) {
                    log.warn("이미지 파일을 읽을 수 없습니다: {}", file.getOriginalFilename());
                    continue;
                }
                
                ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                Thumbnails.of(originalImage)
                        .size(400, 300)
                        .outputFormat("JPEG")
                        .toOutputStream(thumbnailOutputStream);
                
                // 3. EventImage 엔티티 생성 및 저장
                EventImage image = new EventImage();
                image.setEvent(event);
                image.setImageUrl(imageUrl);
                image.setImageData(fileBytes);
                image.setThumbnailData(thumbnailOutputStream.toByteArray());
                image.setOrderIndex(idx++);
                image.setIsThumbnail(idx == 1); // 첫 번째 이미지를 대표사진으로 설정
                
                eventImageRepository.save(image);
                
                log.info("이벤트 이미지 저장 완료: eventId={}, imageUrl={}", event.getEventId(), imageUrl);
                
            } catch (IOException e) {
                log.error("이벤트 이미지 저장 실패: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("이벤트 이미지 저장 실패: " + file.getOriginalFilename(), e);
            } catch (Exception e) {
                log.error("이벤트 이미지 처리 중 예상치 못한 오류: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("이벤트 이미지 처리 실패: " + file.getOriginalFilename(), e);
            }
        }
    }

    @Transactional
    public void deleteEvent(Long id, String aid) {
        Event event = eventRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        // 이벤트 이미지 파일 삭제
        List<EventImage> images = eventImageRepository.findByEvent_EventId(id);
        for (EventImage img : images) {
            if (img.getImageUrl() != null) {
                fileStorageUtil.deleteImage(img.getImageUrl());
            }
        }

        // 본문에서 추출한 이미지 파일 삭제
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

        log.info("base: -------->{}", dto);
        return base;
    }

    public List<byte[]> getEventImageDataList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getImageData)
                .collect(Collectors.toList());
    }

    public List<byte[]> getEventThumbnailDataList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getThumbnailData)
                .collect(Collectors.toList());
    }

    public List<String> getEventImageUrlList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getImageUrl)
                .collect(Collectors.toList());
    }

    public EventImage findEventImageById(Long imageId) {
        return eventImageRepository.findById(imageId).orElse(null);
    }

    @Transactional
    public void deleteEventImages(Long eventId) {
        List<EventImage> images = eventImageRepository.findByEvent_EventId(eventId);
        
        if (images.isEmpty()) {
            log.info("삭제할 이미지가 없습니다: eventId={}", eventId);
            return;
        }

        // 파일시스템에서 이미지 파일 삭제
        for (EventImage image : images) {
            if (image.getImageUrl() != null) {
                try {
                    fileStorageUtil.deleteImage(image.getImageUrl());
                } catch (Exception e) {
                    log.warn("이미지 파일 삭제 실패: {}", image.getImageUrl(), e);
                    // 파일 삭제 실패해도 DB 삭제는 계속 진행
                }
            }
        }

        // DB에서 이미지 레코드 삭제
        eventImageRepository.deleteAll(images);
        
        log.info("이벤트 이미지 삭제 완료: eventId={}, 삭제된 이미지 수={}", eventId, images.size());
    }
}