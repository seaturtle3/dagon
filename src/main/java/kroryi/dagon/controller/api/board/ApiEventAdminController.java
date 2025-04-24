package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.auth.AdminUserDetails;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이벤트(관리자)", description = "관리자용 이벤트 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/events")
@PreAuthorize("hasRole('ADMIN')")
public class ApiEventAdminController {

    private final EventService eventService;

    @Operation(summary = "이벤트 등록", description = "관리자가 새 이벤트를 등록합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EventRequestDTO dto,
                                    BindingResult result,
                                    @AuthenticationPrincipal AdminUserDetails admin) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        String adminId = admin.getAid();
        Event event = eventService.createEvent(dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }

    @Operation(summary = "이벤트 수정", description = "이벤트 내용을 수정합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id,
                                                   @RequestBody EventRequestDTO dto,
                                                   @AuthenticationPrincipal AdminUserDetails admin) {
        String adminId = admin.getAid();
        Event event = eventService.updateEvent(id, dto, adminId);
        return ResponseEntity.ok(EventResponseDTO.from(event));
    }

    @Operation(summary = "이벤트 삭제", description = "이벤트를 삭제합니다.", security = @SecurityRequirement(name = "BearerAuth"))
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}