package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeWebController {
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
    public String save(@ModelAttribute NoticeRequestDTO dto) {
        noticeService.createNotice(dto, "admin001"); // 테스트용
        return "redirect:/notices";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("notices", noticeService.findAll());
        return "board/notice/list";
    }
}
