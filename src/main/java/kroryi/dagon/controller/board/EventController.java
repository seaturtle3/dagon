package kroryi.dagon.controller.board;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    // 목록 조회
    @GetMapping
    public String readAll(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> pagedEvents = eventService.findAllPaged(pageable);

        model.addAttribute("events", pagedEvents.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedEvents.getTotalPages());
        model.addAttribute("size", size);
        return "board/event/list";
    }

    // 단건 조회
    @GetMapping("/{id}")
    public String readOne(@PathVariable Long id, Model model) {
        eventService.increaseViews(id);
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        return "board/event/read";
    }

    // 등록 폼
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("eventForm", new EventRequestDTO());
        model.addAttribute("formAction", "/event");
        return "board/event/form";
    }

    // 등록 처리
    @PostMapping
    public String create(@Valid @ModelAttribute("eventForm") EventRequestDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/event");
            return "board/event/form";
        }
        eventService.createEvent(dto, "admin001");
        return "redirect:/event";
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Event event = eventService.findById(id);
        EventRequestDTO dto = new EventRequestDTO();
        dto.setTitle(event.getTitle());
        dto.setContent(event.getContent());
        dto.setThumbnailUrl(event.getThumbnailUrl());
        dto.setStartAt(event.getStartAt());
        dto.setEndAt(event.getEndAt());
        dto.setIsTop(event.getIsTop());

        model.addAttribute("eventForm", dto);
        model.addAttribute("eventId", id);
        model.addAttribute("formAction", "/event/" + id + "/edit");
        return "board/event/form";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute EventRequestDTO dto) {
        eventService.updateEvent(id, dto);
        return "redirect:/event/" + id;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/event";
    }

}
