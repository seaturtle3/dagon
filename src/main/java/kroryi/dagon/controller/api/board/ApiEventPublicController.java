package kroryi.dagon.controller.api.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventResponseDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이벤트", description = "사용자용 이벤트 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
public class ApiEventPublicController {

    private final EventService eventService;

    @Operation(summary = "이벤트 목록 조회", description = "전체 이벤트를 페이징 및 검색 조건으로 조회합니다.")
    @GetMapping
    public Page<EventResponseDTO> getAllPaged(
            @ModelAttribute BoardSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return eventService.searchEvents(searchDTO, pageable)
                .map(EventResponseDTO::from);
    }

    @Operation(summary = "이벤트 단건 조회", description = "이벤트 상세 내용을 조회하고 조회수를 1 증가시킵니다.")
    @GetMapping("/{id}")
    public EventResponseDTO getOne(@PathVariable Long id) {
        eventService.increaseViews(id);
        Event event = eventService.findById(id);
        return EventResponseDTO.from(event);
    }
}