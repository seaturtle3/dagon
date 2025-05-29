package kroryi.dagon.controller.admin.community;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.EventRequestDTO;
import kroryi.dagon.entity.Event;
import kroryi.dagon.service.community.EventService;
import kroryi.dagon.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/event")
@RequiredArgsConstructor
public class AdminEventViewController {
    private final EventService eventService;

    // 목록 조회
    @GetMapping
    public String readAll(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String type,
                          Model model) {


        BoardSearchDTO searchDTO = new BoardSearchDTO();
        searchDTO.setKeyword(keyword);
        searchDTO.setType(type);

        Page<Event> paged = eventService.searchEvents(searchDTO, PageRequest.of(page, size));

        if (page >= paged.getTotalPages() && paged.getTotalPages() > 0) {
            return "redirect:/admin/event?page=" + (paged.getTotalPages() - 1) + "&size=" + size;
        }

        model.addAttribute("eventPage", paged);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(paged));
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword); // 검색 유지
        model.addAttribute("type", type);       // 검색 유지
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
        model.addAttribute("formAction", "/admin/event?page=" + page + "&size=" + size);
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
            model.addAttribute("formAction", "/admin/event?page=" + page + "&size=" + size);
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            return "board/event/form";
        }

        eventService.createEvent(dto, "admin001");

        // ✅ 등록 후 대시보드로 이동
        return "redirect:/admin/dashboard";
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
        model.addAttribute("formAction", "/admin/event/" + id + "?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/event/form";
    }

    // 수정 처리
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute EventRequestDTO dto) {
        eventService.updateEvent(id, dto);
        return "redirect:/admin/dashboard";  // ✅ 수정 후 대시보드로 이동
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/admin/dashboard";  // ✅ 원하는 경로로 이동
    }

}
