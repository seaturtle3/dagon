package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class ApiEventPublicController {

    private final EventService eventService;

    @Operation(summary = "이벤트 목록 조회", description = "전체 이벤트 페이징 방식으로 조회")
    @GetMapping
    public Page<EventResponseDTO> getAllPaged(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventService.findAllPaged(pageable)
                .map(EventResponseDTO::from);
    }

    @Operation(summary = "이벤트 단건 조회", description = "이벤트 상세 내용을 조회 조회수를 1 증가")
    @GetMapping("/{id}")
    public EventResponseDTO getOne(@PathVariable Long id) {
        eventService.increaseViews(id);
        Event event = eventService.findById(id);
        return EventResponseDTO.from(event);
    }
}
