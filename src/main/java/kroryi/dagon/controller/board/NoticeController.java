package kroryi.dagon.controller.board;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/{id}")
    public String getNoticeRead(@PathVariable Long id, Model model) {
        model.addAttribute("notice", noticeService.findById(id));
        return "board/notice/read";
    }

    @GetMapping("/new")
    public String writeForm(Model model) {
        model.addAttribute("noticeForm", new NoticeRequestDTO());
        return "board/notice/form";
    }

    @PostMapping
    public String save(@Valid @ModelAttribute("noticeForm") NoticeRequestDTO dto,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "board/notice/form"; // 다시 작성 폼으로 이동
        }
        noticeService.createNotice(dto, "admin001"); // 테스트용
        return "redirect:/notices";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notices", noticeService.findAll());
        return "board/notice/list";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Notice notice = noticeService.findById(id);
        NoticeRequestDTO dto = new NoticeRequestDTO();
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setThumbnailUrl(notice.getThumbnailUrl());
        dto.setIsTop(notice.getIsTop());

        model.addAttribute("noticeForm", dto);
        model.addAttribute("noticeId", notice.getNoticeId());
        return "board/notice/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute NoticeRequestDTO dto) {
        noticeService.updateNotice(id, dto);
        return "redirect:/notices/" + id;
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return "redirect:/notices";
    }
}
