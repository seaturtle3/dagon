package kroryi.dagon.controller.base.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.entity.EventImage;
import kroryi.dagon.service.community.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Event", description = "이벤트 조회 API (사용자)")
@RequestMapping("/api/event")
public class ApiEventController {

    private final EventService eventService;

    @GetMapping
    @Operation(summary = "이벤트 목록 조회", description = "전체 이벤트 페이징 + 검색")
    public Page<EventResponseDTO> getAllPaged(
            @ModelAttribute BoardSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return eventService.searchEvents(searchDTO, pageable)
                .map(EventResponseDTO::from);
    }

    @GetMapping("/list")
    public List<EventResponseDTO> getAllEvents() {
        return eventService.getAllEvent().stream()
                .map(EventResponseDTO::from)
                .toList();
    }

    @Operation(summary = "이벤트 단건 조회", description = "이벤트 상세 내용을 조회 조회수를 1 증가")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getOne(@PathVariable Long id) {
        eventService.increaseViews(id);
        Event event = eventService.findById(id);
        
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        
        EventResponseDTO responseDTO = EventResponseDTO.from(event);
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "이벤트 이미지 조회", description = "이벤트의 개별 이미지를 조회")
    @GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getEventImage(@PathVariable Long imageId) {
        EventImage image = eventService.findEventImageById(imageId);
        if (image == null || image.getImageData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.getImageData().length);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getImageData());
    }

    @Operation(summary = "이벤트 썸네일 조회", description = "이벤트의 개별 썸네일을 조회")
    @GetMapping("/thumbnail/{imageId}")
    public ResponseEntity<byte[]> getEventThumbnail(@PathVariable Long imageId) {
        EventImage image = eventService.findEventImageById(imageId);
        if (image == null || image.getThumbnailData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(image.getThumbnailData().length);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getThumbnailData());
    }
}
