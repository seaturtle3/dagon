package kroryi.dagon.controller.admin.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.community.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Event", description = "이벤트관리 API (관리자)")
@RequestMapping("/api/admin/event")
public class ApiAdminEventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 등록", description = "관리자가 새로운 이벤트 등록")
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestPart("dto") EventRequestDTO dto, BindingResult result,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal AdminUserDetails userDetails) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        dto.setImages(images);
        String adminId = userDetails.getAid();
        Event event = eventService.createEvent(dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }

    @Operation(summary = "이벤트 수정", description = "기존 이벤트 정보수정")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(
            @PathVariable Long id,
            @RequestPart("dto") EventRequestDTO dto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal AdminUserDetails userDetails) {
        dto.setImages(images);
        String adminId = userDetails.getAid();
        Event event = eventService.updateEvent(id, dto, adminId);
        
        // 수정된 이벤트의 이미지 ID 리스트를 포함하여 반환
        EventResponseDTO responseDTO = EventResponseDTO.from(event);
        
        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "이벤트 삭제", description = "해당 이벤트 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AdminUserDetails userDetails) {

        String adminId = userDetails.getAid();
        eventService.deleteEvent(id, adminId);

        return ResponseEntity.noContent().build();
    }
}