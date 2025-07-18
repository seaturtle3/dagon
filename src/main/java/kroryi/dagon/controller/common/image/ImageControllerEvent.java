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
import java.util.ArrayList;
import java.util.HashMap;
import kroryi.dagon.entity.Event;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @PostMapping(value = "/upload", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> uploadImage(
        @Parameter(description = "업로드할 이미지 파일", required = true)
        @RequestPart("image") MultipartFile file,
        @RequestParam(value = "eventId", required = false) Long eventId,
        @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") Boolean isThumbnail,
        @RequestParam(value = "imageType", required = false) String imageType
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

        // 파라미터로 받은 값들 설정
        image.setIsThumbnail(isThumbnail);
        image.setImageType(imageType);

        // 썸네일 생성
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
        Thumbnails.of(originalImage)
                .size(400, 300)
                .outputFormat("JPEG")
                .toOutputStream(thumbnailOutputStream);
        image.setThumbnailData(thumbnailOutputStream.toByteArray());

        // 이벤트 연결 (eventId가 있는 경우에만)
        if (eventId != null) {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event != null) {
                image.setEvent(event);
                // orderIndex 설정
                List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(eventId);
                image.setOrderIndex(existingImages.size());
            } else {
                // event가 없는 경우 orderIndex를 0으로 설정
                image.setOrderIndex(0);
            }
            eventImageRepository.save(image);
            String dbUrl = "/api/images/event/" + image.getId();

            // 3. content 업데이트 (이미지 URL을 DB URL로 교체)
            updateContentWithImageUrl(event, fileUrl, dbUrl);

            // 4. 두 URL 모두 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("fileUrl", fileUrl, "dbUrl", dbUrl));
        }

        // eventId가 없거나 이벤트를 찾을 수 없는 경우
        eventImageRepository.save(image);
        String dbUrl = "/api/images/event/" + image.getId();

        // 3. 파일 URL만 반환 (content 업데이트 없음)
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("fileUrl", fileUrl, "dbUrl", dbUrl));
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
    public ResponseEntity<Map<String, String>> uploadEditorImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") Boolean isThumbnail,
            @RequestParam(value = "imageType", required = false) String imageType) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "빈 파일입니다."));
        }

        Event event = null;
        if (eventId != null) {
            event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "존재하지 않는 이벤트입니다."));
            }
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

            // 파라미터로 받은 값들 설정
            image.setIsThumbnail(isThumbnail);
            image.setImageType(imageType);

            if (event != null) {
                image.setEvent(event);

                // orderIndex 설정
                List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(eventId);
                image.setOrderIndex(existingImages.size());
            } else {
                // event가 없는 경우 orderIndex를 0으로 설정
                image.setOrderIndex(0);
            }

            // 4. 썸네일 생성 (안전하게 처리)
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileBytes));
            if (originalImage == null) {
                log.warn("이미지 파일을 읽을 수 없습니다: {}", file.getOriginalFilename());
                return ResponseEntity.badRequest().body(Map.of("error", "지원하지 않는 이미지 형식입니다."));
            }

            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(400, 300)
                    .outputFormat("JPEG")
                    .toOutputStream(thumbnailOutputStream);
            image.setThumbnailData(thumbnailOutputStream.toByteArray());

            eventImageRepository.save(image);

            log.info("image---------------->: {}", image);

            // 5. DB URL 반환 (에디터에서 사용) - 상대 경로만 사용
            String dbUrl = "/api/images/event/" + image.getId();

            // 6. content 업데이트 (이미지 URL을 DB URL로 교체) - event가 있는 경우에만
            if (event != null) {
                updateContentWithImageUrl(event, fileUrl, dbUrl);
            }


            log.info("image dbulr---------------->: {}", dbUrl);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                        "fileUrl", fileUrl,
                        "dbUrl", dbUrl,
                        "imageId", image.getId().toString(),
                        "message", "에디터 이미지 업로드 완료"
                    ));

        } catch (IOException e) {
            log.error("에디터 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 업로드 실패: " + e.getMessage()));
        } catch (Exception e) {
            log.error("에디터 이미지 처리 중 예상치 못한 오류: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 처리 실패: " + e.getMessage()));
        }
    }

    // DB에 저장된 이미지 반환
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long id) {
        log.info("id---------------->: {}", id);
        EventImage image = eventImageRepository.findById(id).orElse(null);
        if (image == null) {
            log.warn("이미지를 찾을 수 없습니다: imageId={}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getImageData());
    }

    // DB에 저장된 썸네일 반환
    @GetMapping("/{id}/thumb")
    public ResponseEntity<byte[]> getEventThumbnail(@PathVariable Long id) {
        EventImage image = eventImageRepository.findById(id).orElse(null);
        if (image == null) {
            log.warn("썸네일을 찾을 수 없습니다: imageId={}", id);
            return ResponseEntity.notFound().build();
        }

        // isThumbnail이 true인 경우에만 썸네일 반환
        if (!image.getIsThumbnail()) {
            log.warn("썸네일이 아닌 이미지입니다: imageId={}", id);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image.getThumbnailData());
    }

    /**
     * 저장된 모든 이미지 ID 목록 조회 (디버깅용)
     */
    @GetMapping("/list")
    @Operation(summary = "이미지 목록 조회", description = "저장된 모든 이미지 ID 목록을 조회")
    public ResponseEntity<Map<String, Object>> getAllImageIds() {
        try {
            List<EventImage> allImages = eventImageRepository.findAll();
            List<Map<String, Object>> imageInfo = allImages.stream()
                    .map(img -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", img.getId());
                        map.put("eventId", img.getEvent() != null ? img.getEvent().getEventId() : null);
                        map.put("imageUrl", img.getImageUrl());
                        map.put("isThumbnail", img.getIsThumbnail());
                        map.put("imageType", img.getImageType());
                        map.put("orderIndex", img.getOrderIndex());
                        return map;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                "totalCount", allImages.size(),
                "images", imageInfo
            ));

        } catch (Exception e) {
            log.error("이미지 목록 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이미지 목록 조회 중 오류가 발생했습니다: " + e.getMessage()));
        }
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
        return eventImageRepository.findByEvent_EventIdAndIsThumbnailTrue(eventId).stream()
            .map(EventImage::getImageData)
            .collect(Collectors.toList());
    }

    public List<byte[]> getEventThumbnailDataList(Long eventId) {
        return eventImageRepository.findByEvent_EventIdAndIsThumbnailTrue(eventId).stream()
            .map(EventImage::getThumbnailData)
            .collect(Collectors.toList());
    }

    /**
     * content에서 이미지 태그를 추출하여 DB에 저장하고, content에서 이미지 태그를 제거
     */
    @PostMapping("/process-content")
    @Operation(summary = "컨텐츠 이미지 처리", description = "content에서 이미지를 추출하여 DB에 저장하고 content 정리")
    public ResponseEntity<Map<String, Object>> processContentImages(
            @RequestParam("eventId") Long eventId,
            @RequestParam("content") String content) {

        try {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "존재하지 않는 이벤트입니다."));
            }

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

            // 이벤트 content 업데이트
            event.setContent(processedContent);
            eventRepository.save(event);

            return ResponseEntity.ok(Map.of(
                "processedContent", processedContent,
                "extractedImages", extractedImages,
                "message", "컨텐츠 처리가 완료되었습니다."
            ));

        } catch (Exception e) {
            log.error("컨텐츠 이미지 처리 중 오류 발생: eventId={}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "컨텐츠 처리 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 이미지 URL을 DB에 저장하는 메서드
     */
    @PostMapping("/save-image-url")
    @Operation(summary = "이미지 URL 저장", description = "이미지 URL을 DB에 저장")
    public ResponseEntity<String> saveImageUrl(
            @RequestParam(value = "eventId", required = false) Long eventId,
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") Boolean isThumbnail,
            @RequestParam(value = "imageType", required = false) String imageType) {

        try {
            Event event = null;
            if (eventId != null) {
                event = eventRepository.findById(eventId).orElse(null);
                if (event == null) {
                    return ResponseEntity.badRequest().body("존재하지 않는 이벤트입니다.");
                }
            }

            // 이미지 파일 경로에서 실제 파일 읽기
            String relativePath = imageUrl;
            if (relativePath.startsWith("/uploads/")) {
                relativePath = relativePath.substring(1); // "/" 제거
            }

            String projectRoot = System.getProperty("user.dir");
            Path filePath = Paths.get(projectRoot, relativePath);

            if (!Files.exists(filePath)) {
                return ResponseEntity.badRequest().body("이미지 파일을 찾을 수 없습니다: " + imageUrl);
            }

            // 파일 읽기
            byte[] imageData = Files.readAllBytes(filePath);

            // 썸네일 생성
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
            if (originalImage == null) {
                return ResponseEntity.badRequest().body("지원하지 않는 이미지 형식입니다.");
            }

            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(400, 300)
                    .outputFormat("JPEG")
                    .toOutputStream(thumbnailOutputStream);

            // EventImage 엔티티 생성 및 저장
            EventImage image = new EventImage();
            if (event != null) {
                image.setEvent(event);

                // orderIndex 설정
                List<EventImage> existingImages = eventImageRepository.findByEvent_EventId(eventId);
                image.setOrderIndex(existingImages.size());
            } else {
                // event가 없는 경우 orderIndex를 0으로 설정
                image.setOrderIndex(0);
            }
            image.setImageUrl(imageUrl);
            image.setImageData(imageData);
            image.setThumbnailData(thumbnailOutputStream.toByteArray());
            image.setIsThumbnail(isThumbnail);
            image.setImageType(imageType);

            eventImageRepository.save(image);

            // content 업데이트 (이미지 URL을 DB URL로 교체) - event가 있는 경우에만
            String dbUrl = "/api/images/event/" + image.getId();
            if (event != null) {
                updateContentWithImageUrl(event, imageUrl, dbUrl);
            }

            return ResponseEntity.ok("이미지 URL이 성공적으로 저장되었습니다.");

        } catch (Exception e) {
            log.error("이미지 URL 저장 중 오류 발생: eventId={}, imageUrl={}", eventId, imageUrl, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("이미지 URL 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 임시 이미지 업로드 (이벤트 등록 전)
     */
    @PostMapping("/temp-upload")
    @Operation(summary = "임시 이미지 업로드", description = "이벤트 등록 전 임시로 이미지를 업로드")
    public ResponseEntity<Map<String, String>> uploadTempImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") Boolean isThumbnail,
            @RequestParam(value = "imageType", required = false) String imageType) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "빈 파일입니다."));
        }

        try {
            // 1. 파일시스템 저장
            String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadPath = Paths.get(baseUploadDir).resolve(dateFolder);
            Files.createDirectories(uploadPath);
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            String storedFileName = UUID.randomUUID() + "." + ext;
            Path targetPath = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "/uploads/" + dateFolder + "/" + storedFileName;

            // 2. DB 저장 (이벤트 연결 없이)
            EventImage image = new EventImage();
            image.setImageData(file.getBytes());
            image.setImageUrl(fileUrl);
            image.setIsThumbnail(isThumbnail);
            image.setImageType(imageType != null ? imageType : "temp");
            image.setOrderIndex(0); // 임시 이미지는 orderIndex 0

            // 썸네일 생성
            BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(400, 300)
                    .outputFormat("JPEG")
                    .toOutputStream(thumbnailOutputStream);
            image.setThumbnailData(thumbnailOutputStream.toByteArray());

            eventImageRepository.save(image);

            // 3. DB URL 반환 (에디터에서 사용)
            String dbUrl = "/api/images/event/" + image.getId();

            return ResponseEntity.ok(Map.of(
                "fileUrl", fileUrl,
                "dbUrl", dbUrl,
                "imageId", image.getId().toString(),
                "message", "임시 이미지 업로드 완료"
            ));

        } catch (Exception e) {
            log.error("임시 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "임시 이미지 업로드 실패: " + e.getMessage()));
        }
    }

    /**
     * 임시 이미지를 이벤트에 연결
     */
    @PostMapping("/link-temp-images")
    @Operation(summary = "임시 이미지 연결", description = "임시 이미지들을 특정 이벤트에 연결")
    public ResponseEntity<String> linkTempImagesToEvent(
            @RequestParam("eventId") Long eventId,
            @RequestParam("imageIds") List<Long> imageIds) {

        try {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event == null) {
                return ResponseEntity.badRequest().body("존재하지 않는 이벤트입니다.");
            }

            int orderIndex = 0;
            for (Long imageId : imageIds) {
                EventImage image = eventImageRepository.findById(imageId).orElse(null);
                if (image != null && image.getEvent() == null) {
                    // 임시 이미지를 이벤트에 연결
                    image.setEvent(event);
                    image.setOrderIndex(orderIndex++);
                    image.setImageType("content"); // temp에서 content로 변경
                    eventImageRepository.save(image);

                    log.info("임시 이미지 연결 완료: imageId={}, eventId={}", imageId, eventId);
                }
            }

            return ResponseEntity.ok("임시 이미지 연결이 완료되었습니다. (연결된 이미지 수: " + orderIndex + ")");

        } catch (Exception e) {
            log.error("임시 이미지 연결 중 오류 발생: eventId={}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("임시 이미지 연결 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 임시 이미지 정리 (연결되지 않은 이미지들 삭제)
     */
    @DeleteMapping("/cleanup-temp-images")
    @Operation(summary = "임시 이미지 정리", description = "연결되지 않은 임시 이미지들을 삭제")
    public ResponseEntity<String> cleanupTempImages() {
        try {
            // 이벤트에 연결되지 않은 임시 이미지들 조회
            List<EventImage> tempImages = eventImageRepository.findByEventIsNull();

            if (tempImages.isEmpty()) {
                return ResponseEntity.ok("정리할 임시 이미지가 없습니다.");
            }

            int deletedCount = 0;
            for (EventImage image : tempImages) {
                try {
                    // 파일시스템에서 이미지 파일 삭제
                    if (image.getImageUrl() != null) {
                        String relativePath = image.getImageUrl();
                        if (relativePath.startsWith("/uploads/")) {
                            relativePath = relativePath.substring(1);
                        }

                        String projectRoot = System.getProperty("user.dir");
                        Path filePath = Paths.get(projectRoot, relativePath);

                        if (Files.exists(filePath)) {
                            Files.delete(filePath);
                            log.info("임시 이미지 파일 삭제 완료: {}", filePath);
                        }
                    }

                    // DB에서 이미지 레코드 삭제
                    eventImageRepository.delete(image);
                    deletedCount++;

                } catch (Exception e) {
                    log.warn("임시 이미지 삭제 실패: imageId={}", image.getId(), e);
                }
            }

            log.info("임시 이미지 정리 완료: 삭제된 이미지 수={}", deletedCount);
            return ResponseEntity.ok("임시 이미지 정리가 완료되었습니다. (삭제된 이미지 수: " + deletedCount + ")");

        } catch (Exception e) {
            log.error("임시 이미지 정리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("임시 이미지 정리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * content에서 이미지 URL을 DB URL로 교체
     */
    private void updateContentWithImageUrl(Event event, String oldUrl, String newUrl) {
        try {
            String content = event.getContent();
            if (content != null && content.contains(oldUrl)) {
                // content에서 파일 URL을 DB URL로 교체
                String updatedContent = content.replace(oldUrl, newUrl);
                event.setContent(updatedContent);
                eventRepository.save(event);
                log.info("Content 업데이트 완료: eventId={}, oldUrl={}, newUrl={}",
                        event.getEventId(), oldUrl, newUrl);
            }
        } catch (Exception e) {
            log.error("Content 업데이트 중 오류 발생: eventId={}, oldUrl={}, newUrl={}",
                    event.getEventId(), oldUrl, newUrl, e);
        }
    }
}
