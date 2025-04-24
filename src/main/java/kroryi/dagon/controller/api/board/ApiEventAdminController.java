package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin/event")
public class ApiEventAdminController {

    private final EventService eventService;

    @Operation(summary = "이벤트 등록", description = "관리자가 새로운 이벤트 등록")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EventRequestDTO dto,
                                    BindingResult result,
                                    Authentication auth) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String adminId = auth.getName(); // ✅ 현재 로그인한 관리자 ID
        Event event = eventService.createEvent(dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }

    @PostMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id,
                                                   @RequestBody EventRequestDTO dto,
                                                   Authentication auth) {
        String adminId = auth.getName();
        Event event = eventService.updateEvent(id, dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }


    @Operation(summary = "이벤트 삭제", description = "해당 이벤트 삭제")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}