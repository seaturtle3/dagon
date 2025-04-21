package kroryi.dagon.controller.board;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.board.EventService;
import kroryi.dagon.util.PaginationUtil;
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
        Page<Event> paged = eventService.findAllPaged(PageRequest.of(page, size));
        if (page >= paged.getTotalPages() && paged.getTotalPages() > 0) {
            return "redirect:/event?page=" + (paged.getTotalPages() - 1) + "&size=" + size;
        }

        model.addAttribute("eventPage", paged);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(paged));
        model.addAttribute("size", size);
        return "board/event/list";
    }

    // 단건 조회
    @GetMapping("/{id}")
    public String readOne(@PathVariable Long id,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        eventService.increaseViews(id);
        model.addAttribute("event", eventService.findById(id));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/event/read";
    }

    // 등록 폼
    @GetMapping("/create")
    public String createForm(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        model.addAttribute("eventForm", new EventRequestDTO());
        model.addAttribute("formAction", "/event?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/event/form";
    }

    // 등록 처리
    @PostMapping
    public String create(@Valid @ModelAttribute("eventForm") EventRequestDTO dto,
                         BindingResult result,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("formAction", "/event?page=" + page + "&size=" + size);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            return "board/event/form";
        }
        eventService.createEvent(dto, "admin001");
        return "redirect:/event?page=" + page + "&size=" + size;
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
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
        model.addAttribute("formAction", "/event/" + id + "?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/event/form";
    }

    // 수정 처리
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute EventRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        eventService.updateEvent(id, dto);
        return "redirect:/event?page=" + page + "&size=" + size;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        eventService.deleteEvent(id);
        return "redirect:/event?page=" + page + "&size=" + size;
    }

}
