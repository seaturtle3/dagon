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
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;


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
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setAdmin(admin);
        event.setContent(dto.getContent());

        // 썸네일 처리 (base64 or url)
        String thumbnailUrl = null;
        if (dto.getThumbnailUrl() != null && dto.getThumbnailUrl().startsWith("data:")) {
            try {
                String[] parts = dto.getThumbnailUrl().split(",");
                String base64Data = parts.length > 1 ? parts[1] : parts[0];
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                String dateFolder = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                Path uploadPath = Paths.get("uploads/event", dateFolder);
                Files.createDirectories(uploadPath);
                String fileName = java.util.UUID.randomUUID() + ".jpg";
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageBytes);
                thumbnailUrl = "/uploads/event/" + dateFolder + "/" + fileName;
            } catch (Exception e) {
                log.error("base64 썸네일 저장 실패", e);
            }
        } else if (dto.getThumbnailUrl() != null) {
            thumbnailUrl = dto.getThumbnailUrl();
        }
        event.setThumbnailUrl(thumbnailUrl);

        Event saved = eventRepository.save(event);
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            saveEventImages(saved, dto.getImages());
        }
        return saved;
    }

    @Transactional
    public Event updateEvent(Long id, EventRequestDTO dto, String aid) {
        Event event = eventRepository.findById(id).orElseThrow();
        Admin admin = adminRepository.findById(aid).orElseThrow();

        // 기존 이미지 파일 삭제 (EventImage)
        List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(id);
        for (EventImage img : existingImages) {
            if (img.getImageUrl() != null) {
                fileStorageUtil.deleteImage(img.getImageUrl());
            }
        }

        event.setTitle(dto.getTitle());
        event.setStartAt(dto.getStartAt());
        event.setEndAt(dto.getEndAt());
        event.setIsTop(dto.getIsTop() != null && dto.getIsTop());
        event.setModifyAt(LocalDateTime.now());
        event.setAdmin(admin);
        event.setContent(dto.getContent());

        // 썸네일 처리 (base64 or url)
        String thumbnailUrl = null;
        byte[] imageBytes = null;
        byte[] thumbnailBytes = null;
        if (dto.getThumbnailUrl() != null && dto.getThumbnailUrl().startsWith("data:")) {
            try {
                // 기존 썸네일 파일 삭제
                if (event.getThumbnailUrl() != null) {
                    fileStorageUtil.deleteImage(event.getThumbnailUrl());
                }
                String[] parts = dto.getThumbnailUrl().split(",");
                String base64Data = parts.length > 1 ? parts[1] : parts[0];
                imageBytes = Base64.getDecoder().decode(base64Data);
                String dateFolder = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                Path uploadPath = Paths.get("uploads/event", dateFolder);
                Files.createDirectories(uploadPath);
                String fileName = java.util.UUID.randomUUID() + ".jpg";
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, imageBytes);
                thumbnailUrl = "/uploads/event/" + dateFolder + "/" + fileName;

                // 썸네일 리사이즈
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                if (originalImage != null) {
                    ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
                    Thumbnails.of(originalImage)
                            .size(400, 300)
                            .outputFormat("JPEG")
                            .toOutputStream(thumbnailOutputStream);
                    thumbnailBytes = thumbnailOutputStream.toByteArray();
                } else {
                    thumbnailBytes = imageBytes;
                }
            } catch (Exception e) {
                log.error("base64 썸네일 저장 실패", e);
            }
        } else if (dto.getThumbnailUrl() != null) {
            thumbnailUrl = dto.getThumbnailUrl();
        }
        // 썸네일이 변경된 경우에만 set
        if (thumbnailUrl != null) {
            event.setThumbnailUrl(thumbnailUrl);

            // event_image 테이블의 썸네일 레코드 update or insert
            List<EventImage> thumbnails = eventImageRepository.findByEvent_EventIdAndIsThumbnailTrueOrderByOrderIndex(event.getEventId());
            if (!thumbnails.isEmpty()) {
                EventImage thumb = thumbnails.get(0);
                thumb.setImageUrl(thumbnailUrl);
                if (imageBytes != null) thumb.setImageData(imageBytes);
                if (thumbnailBytes != null) thumb.setThumbnailData(thumbnailBytes);
                thumb.setIsThumbnail(true);
                thumb.setImageType("thumbnail");
                thumb.setOrderIndex(0);
                eventImageRepository.save(thumb);
            } else if (imageBytes != null && thumbnailBytes != null) {
                EventImage newThumb = new EventImage();
                newThumb.setEvent(event);
                newThumb.setImageUrl(thumbnailUrl);
                newThumb.setImageData(imageBytes);
                newThumb.setThumbnailData(thumbnailBytes);
                newThumb.setIsThumbnail(true);
                newThumb.setImageType("thumbnail");
                newThumb.setOrderIndex(0);
                eventImageRepository.save(newThumb);
            }
        }

        Event saved = eventRepository.save(event);
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
                image.setIsThumbnail(idx == 0); // 첫 번째 이미지를 대표사진으로 설정
                image.setImageType("content");
                image.setOrderIndex(idx);
                idx++;

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
        return eventImageRepository.findByEvent_EventIdAndIsThumbnailTrueOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getImageData)
                .collect(Collectors.toList());
    }

    public List<byte[]> getEventThumbnailDataList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdAndIsThumbnailTrueOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getThumbnailData)
                .collect(Collectors.toList());
    }

    public List<String> getEventImageUrlList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdAndIsThumbnailTrueOrderByOrderIndex(eventId)
                .stream()
                .map(EventImage::getImageUrl)
                .collect(Collectors.toList());
    }

    /**
     * content에서 이미지를 추출하여 DB에 저장하고 content 정리
     */
    @Transactional
    public Map<String, Object> processContentImages(Long eventId, String content) {
        // 이미지 태그 추출
        Pattern imgPattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>");
        Matcher matcher = imgPattern.matcher(content);

        String processedContent = content;
        List<String> extractedImages = new ArrayList<>();

        // 이미지 태그를 찾아서 추출
        while (matcher.find()) {
            String imgTag = matcher.group(0);
            String imgSrc = matcher.group(1);
            extractedImages.add(imgSrc);

            // content에서 이미지 태그 제거
            processedContent = processedContent.replace(imgTag, "");
        }

        // content 정리 (빈 <p> 태그 제거)
        processedContent = processedContent.replaceAll("<p>\\s*<br>\\s*</p>", "");
        processedContent = processedContent.replaceAll("<p>\\s*</p>", "");
        processedContent = processedContent.trim();

        // eventId가 있는 경우에만 DB 업데이트 및 이미지 저장
        if (eventId != null) {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                throw new RuntimeException("존재하지 않는 이벤트입니다.");
            }

            // 이벤트 content 업데이트
            event.setContent(processedContent);
            eventRepository.save(event);

            // 추출된 이미지들을 DB에 저장
            for (String imageUrl : extractedImages) {
                saveImageFromUrl(event, imageUrl);
            }
        }

        return Map.of(
            "processedContent", processedContent,
            "extractedImages", extractedImages,
            "message", "컨텐츠 처리가 완료되었습니다."
        );
    }

    /**
     * 이미지 URL을 DB에 저장
     */
    @Transactional
    public void saveImageFromUrl(Event event, String imageUrl) {
        try {
            // 이미지 파일 경로에서 실제 파일 읽기
            String relativePath = imageUrl;
            if (relativePath.startsWith("/uploads/")) {
                relativePath = relativePath.substring(1); // "/" 제거
            }

            String projectRoot = System.getProperty("user.dir");
            Path filePath = Paths.get(projectRoot, relativePath);

            if (!Files.exists(filePath)) {
                log.warn("이미지 파일을 찾을 수 없습니다: {}", imageUrl);
                return;
            }

            // 파일 읽기
            byte[] imageData = Files.readAllBytes(filePath);

            // 썸네일 생성
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            if (originalImage == null) {
                log.warn("지원하지 않는 이미지 형식입니다: {}", imageUrl);
                return;
            }

            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(400, 300)
                    .outputFormat("JPEG")
                    .toOutputStream(thumbnailOutputStream);

            // EventImage 엔티티 생성 및 저장
            EventImage image = new EventImage();
            image.setEvent(event);
            image.setImageUrl(imageUrl);
            image.setImageData(imageData);
            image.setThumbnailData(thumbnailOutputStream.toByteArray());
            image.setIsThumbnail(false);
            image.setImageType("content");

            // orderIndex 설정 (기존 이미지 개수 + 1)
            List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(event.getEventId());
            image.setOrderIndex(existingImages.size());

            eventImageRepository.save(image);

            log.info("이미지 URL 저장 완료: eventId={}, imageUrl={}", event.getEventId(), imageUrl);

        } catch (Exception e) {
            log.error("이미지 URL 저장 중 오류 발생: eventId={}, imageUrl={}", event.getEventId(), imageUrl, e);
        }
    }

    public EventImage findEventImageById(Long imageId) {
        return eventImageRepository.findById(imageId).orElse(null);
    }

    @Transactional
    public void deleteEventImages(Long eventId) {
        try {
            List<EventImage> images = eventImageRepository.findByEvent_EventId(eventId);
            eventImageRepository.deleteAll(images);
            log.info("이벤트 이미지 삭제 완료: eventId={}, 삭제된 이미지 수={}", eventId, images.size());
        } catch (Exception e) {
            log.error("이벤트 이미지 삭제 중 오류 발생: eventId={}", eventId, e);
            throw new RuntimeException("이벤트 이미지 삭제 실패", e);
        }
    }
}