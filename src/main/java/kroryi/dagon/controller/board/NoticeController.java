package kroryi.dagon.controller.board;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.board.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // [R] 목록 조회
    @GetMapping
    public String readAll(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notice> pagedNotices = noticeService.findAllPaged(pageable);

        model.addAttribute("notices", pagedNotices.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedNotices.getTotalPages());
        model.addAttribute("size", size);
        return "board/notice/list";
    }

    // [R] 단건 조회
    @GetMapping("/{id}")
    public String readOne(@PathVariable Long id, Model model) {
        noticeService.increaseViews(id);
        model.addAttribute("notice", noticeService.findById(id));
        return "board/notice/read";
    }

    // [C] 등록 폼
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("noticeForm", new NoticeRequestDTO());
        model.addAttribute("formAction", "/notices");
        return "board/notice/form";
    }

    // [C] 등록 처리
    @PostMapping
    public String create(@Valid @ModelAttribute("noticeForm") NoticeRequestDTO dto,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "board/notice/form";
        }
        noticeService.createNotice(dto, "admin001");
        return "redirect:/notices";
    }

    // [U] 수정 폼
    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.findById(id);
        NoticeRequestDTO dto = new NoticeRequestDTO();
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setIsTop(notice.getIsTop());

        model.addAttribute("noticeForm", dto);
        model.addAttribute("noticeId", id);
        model.addAttribute("formAction", "/notices/" + id + "/edit");
        return "board/notice/form";
    }

    // [U] 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute NoticeRequestDTO dto) {
        noticeService.updateNotice(id, dto);
        return "redirect:/notices/" + id;
    }

    // [D] 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }

}
