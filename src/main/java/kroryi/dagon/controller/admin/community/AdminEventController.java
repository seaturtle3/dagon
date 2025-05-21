package kroryi.dagon.controller.admin.community;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Event", description = "이벤트관리 API (관리자)")
@RequestMapping("/api/admin/event")
public class AdminEventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 등록", description = "관리자가 새로운 이벤트 등록")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EventRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String adminId = "admin001"; // 테스트용
        Event event = eventService.createEvent(dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }

    @Operation(summary = "이벤트 수정", description = "기존 이벤트 정보수정")
    @PostMapping("/{id}")
    public EventResponseDTO update(@PathVariable Long id, @RequestBody EventRequestDTO dto) {
        Event event = eventService.updateEvent(id, dto);
        return EventResponseDTO.from(event);
    }

    @Operation(summary = "이벤트 삭제", description = "해당 이벤트 삭제")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}