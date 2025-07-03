package kroryi.dagon.controller.common.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.service.community.EventService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import kroryi.dagon.entity.EventImage;
import kroryi.dagon.repository.board.EventImageRepository;
import kroryi.dagon.repository.board.EventRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import kroryi.dagon.entity.Event;

@Tag(name = "Image", description = "이미지 업로드 및 조회 API")
@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/images/event")
public class ImageControllerEvent {

    @Value("${app.board.file.upload-dir}")
    private String baseUploadDir;

    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드 API")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Map<String, String>> uploadImage(
        @Parameter(description = "업로드할 이미지 파일", required = true)
        @RequestPart("image") MultipartFile file,
        @RequestParam("eventId") Long eventId
    ) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "빈파일"));
        }
        // 1. 파일시스템 저장
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Path uploadPath = Paths.get(baseUploadDir).resolve(dateFolder);
        Files.createDirectories(uploadPath);
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String storedFileName = UUID.randomUUID() + "." + ext;
        Path targetPath = uploadPath.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        String fileUrl = "/uploads/" + dateFolder + "/" + storedFileName;

        // 2. DB 저장
        EventImage image = new EventImage();
        image.setImageData(file.getBytes());
        // 썸네일 생성
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(400, 300)
                .outputFormat("JPEG")
                .toOutputStream(thumbnailOutputStream);
        image.setThumbnailData(thumbnailOutputStream.toByteArray());
        // 이벤트 연결
        Event event = eventRepository.findById(eventId).orElseThrow();
        image.setEvent(event);
        eventImageRepository.save(image);
        String dbUrl = "/api/event/image/" + image.getId();
        // 3. 두 URL 모두 반환
        return ResponseEntity.ok(Map.of("fileUrl", fileUrl, "dbUrl", dbUrl));
    }

    @Operation(summary = "이미지 조회", description = "저장된 이미지 파일을 URL 경로를 통해 조회\n" +
            "예: /images/2025/04/21/uuid-filename.png 형식의 경로로 접근")
    @GetMapping("/images/{year}/{month}/{day}/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String year,
                                               @PathVariable String month,
                                               @PathVariable String day,
                                               @PathVariable String filename) throws IOException {
        Path path = Paths.get(baseUploadDir, year, month, day, filename);
        log.info("pat--->: {}", path);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 에디터 이미지 업로드 (파일+DB 동시 저장)
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadEditorImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId) throws IOException {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("빈 파일입니다.");
        }
        
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.badRequest().body("존재하지 않는 이벤트입니다.");
        }
        
        try {
            // 1. 파일 바이트 배열을 먼저 안전하게 읽기
            byte[] fileBytes = file.getBytes();
            
            // 2. 파일시스템 저장
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadPath = Paths.get(baseUploadDir).resolve(dateFolder);
            Files.createDirectories(uploadPath);
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String storedFileName = UUID.randomUUID() + "." + ext;
            Path targetPath = uploadPath.resolve(storedFileName);
            
            // 파일을 바이트 배열에서 직접 저장
            Files.write(targetPath, fileBytes);
            String fileUrl = "/uploads/" + dateFolder + "/" + storedFileName;
            
            // 3. DB 저장
            EventImage image = new EventImage();
            image.setImageData(fileBytes);
            image.setImageUrl(fileUrl);
            image.setEvent(event);
            
            // 4. 썸네일 생성 (안전하게 처리)
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileBytes));
            if (originalImage == null) {
                log.warn("이미지 파일을 읽을 수 없습니다: {}", file.getOriginalFilename());
                return ResponseEntity.badRequest().body("지원하지 않는 이미지 형식입니다.");
            }
            
            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(400, 300)
                    .outputFormat("JPEG")
                    .toOutputStream(thumbnailOutputStream);
            image.setThumbnailData(thumbnailOutputStream.toByteArray());
            
            eventImageRepository.save(image);
            
            // 5. DB URL 반환 (에디터에서 사용)
            String dbUrl = "/api/event/image/" + image.getId();
            return ResponseEntity.ok(dbUrl);
            
        } catch (IOException e) {
            log.error("에디터 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패: " + e.getMessage());
        } catch (Exception e) {
            log.error("에디터 이미지 처리 중 예상치 못한 오류: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 처리 실패: " + e.getMessage());
        }
    }

    // DB에 저장된 이미지 반환
    @GetMapping("/api/event/image/{id}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long id) {
        EventImage image = eventImageRepository.findById(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getImageData());
    }

    // DB에 저장된 썸네일 반환
    @GetMapping("/api/event/image/{id}/thumb")
    public ResponseEntity<byte[]> getEventThumbnail(@PathVariable Long id) {
        EventImage image = eventImageRepository.findById(id).orElseThrow();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getThumbnailData());
    }

    // 이벤트의 모든 이미지 삭제
    @DeleteMapping("/delete")
    @Operation(summary = "이벤트 이미지 삭제", description = "특정 이벤트의 모든 이미지를 삭제")
    public ResponseEntity<String> deleteEventImages(@RequestParam("eventId") Long eventId) {
        try {
            // 1. 이벤트 존재 확인
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                return ResponseEntity.badRequest().body("존재하지 않는 이벤트입니다.");
            }

            // 2. 이벤트의 모든 이미지 조회
            List<EventImage> images = eventImageRepository.findByEvent_EventId(eventId);
            
            if (images.isEmpty()) {
                return ResponseEntity.ok("삭제할 이미지가 없습니다.");
            }

            // 3. 파일시스템에서 이미지 파일 삭제
            for (EventImage image : images) {
                if (image.getImageUrl() != null) {
                    try {
                        // "/uploads/event/2025/07/02/filename.jpg" 형태의 URL에서 파일 삭제
                        String relativePath = image.getImageUrl();
                        if (relativePath.startsWith("/uploads/")) {
                            relativePath = relativePath.substring(1); // "/" 제거
                        }
                        
                        String projectRoot = System.getProperty("user.dir");
                        Path filePath = Paths.get(projectRoot, relativePath);
                        
                        if (Files.exists(filePath)) {
                            Files.delete(filePath);
                            log.info("이미지 파일 삭제 완료: {}", filePath);
                        }
                    } catch (IOException e) {
                        log.warn("이미지 파일 삭제 실패: {}", image.getImageUrl(), e);
                        // 파일 삭제 실패해도 DB 삭제는 계속 진행
                    }
                }
            }

            // 4. DB에서 이미지 레코드 삭제
            eventImageRepository.deleteAll(images);
            
            log.info("이벤트 이미지 삭제 완료: eventId={}, 삭제된 이미지 수={}", eventId, images.size());
            
            return ResponseEntity.ok("이벤트 이미지가 성공적으로 삭제되었습니다. (삭제된 이미지 수: " + images.size() + ")");
            
        } catch (Exception e) {
            log.error("이벤트 이미지 삭제 중 오류 발생: eventId={}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 개별 이미지 삭제 (선택사항)
    @DeleteMapping("/delete/{imageId}")
    @Operation(summary = "개별 이미지 삭제", description = "특정 이미지만 삭제")
    public ResponseEntity<String> deleteSingleImage(@PathVariable Long imageId) {
        try {
            EventImage image = eventImageRepository.findById(imageId).orElse(null);
            if (image == null) {
                return ResponseEntity.badRequest().body("존재하지 않는 이미지입니다.");
            }

            // 파일시스템에서 이미지 파일 삭제
            if (image.getImageUrl() != null) {
                try {
                    String relativePath = image.getImageUrl();
                    if (relativePath.startsWith("/uploads/")) {
                        relativePath = relativePath.substring(1);
                    }
                    
                    String projectRoot = System.getProperty("user.dir");
                    Path filePath = Paths.get(projectRoot, relativePath);
                    
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                        log.info("개별 이미지 파일 삭제 완료: {}", filePath);
                    }
                } catch (IOException e) {
                    log.warn("개별 이미지 파일 삭제 실패: {}", image.getImageUrl(), e);
                }
            }

            // DB에서 이미지 레코드 삭제
            eventImageRepository.delete(image);
            
            log.info("개별 이미지 삭제 완료: imageId={}", imageId);
            
            return ResponseEntity.ok("이미지가 성공적으로 삭제되었습니다.");
            
        } catch (Exception e) {
            log.error("개별 이미지 삭제 중 오류 발생: imageId={}", imageId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public List<byte[]> getEventImageDataList(Long eventId) {
        return eventImageRepository.findAll().stream()
            .filter(img -> img.getEvent() != null && img.getEvent().getEventId().equals(eventId))
            .map(EventImage::getImageData)
            .collect(Collectors.toList());
    }

    public List<byte[]> getEventThumbnailDataList(Long eventId) {
        return eventImageRepository.findAll().stream()
            .filter(img -> img.getEvent() != null && img.getEvent().getEventId().equals(eventId))
            .map(EventImage::getThumbnailData)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventResponseDTO getOne(@PathVariable Long id) {
        eventService.increaseViews(id);
        Event event = eventService.findById(id);
        EventResponseDTO dto = EventResponseDTO.from(event);
        dto.setImageDataList(eventService.getEventImageDataList(id));
        dto.setThumbnailDataList(eventService.getEventThumbnailDataList(id));
        return dto;
    }
}
