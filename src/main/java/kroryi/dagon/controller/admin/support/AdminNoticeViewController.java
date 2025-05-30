package kroryi.dagon.controller.admin.support;

import jakarta.validation.Valid;
import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.NoticeRequestDTO;
import kroryi.dagon.entity.Notice;
import kroryi.dagon.service.support.NoticeService;
import kroryi.dagon.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeViewController {
    private final NoticeService noticeService;

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

        Page<Notice> paged = noticeService.searchNotices(searchDTO, PageRequest.of(page, size));

        if (page >= paged.getTotalPages() && paged.getTotalPages() > 0) {
            return "redirect:/admin/notice?page=" + (paged.getTotalPages() - 1) + "&size=" + size;
        }

        model.addAttribute("noticePage", paged);
        model.addAttribute("pagination", PaginationUtil.getPaginationData(paged));
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);
        return "board/notice/list";
    }


    // 단건 조회
    @GetMapping("/{id}")
    public String readOne(@PathVariable Long id,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        noticeService.increaseViews(id);
        model.addAttribute("notice", noticeService.findById(id));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/notice/read";
    }
    // 등록 폼
    @GetMapping("/create")
    public String createForm(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        model.addAttribute("noticeForm", new NoticeRequestDTO());
        model.addAttribute("formAction", "/admin/notice?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/notice/form";
    }

    // 등록 처리
    @PostMapping
    public String create(@Valid @ModelAttribute("noticeForm") NoticeRequestDTO dto,
                         BindingResult result,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("page", page);
            model.addAttribute("size", size);
            return "board/notice/form";
        }
        noticeService.createNotice(dto, "admin001");
        return "redirect:/admin/dashboard";  // ✅ 변경됨
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Notice n = noticeService.findById(id);
        NoticeRequestDTO dto = new NoticeRequestDTO();
        dto.setTitle(n.getTitle());
        dto.setContent(n.getContent());
        dto.setIsTop(n.getIsTop());

        model.addAttribute("noticeForm", dto);
        model.addAttribute("noticeId", id);
        model.addAttribute("formAction", "/admin/notice/" + id + "?page=" + page + "&size=" + size);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "board/notice/form";
    }

    // 수정 처리
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute NoticeRequestDTO dto,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        noticeService.updateNotice(id, dto);
        return "redirect:/admin/dashboard";  // ✅ 변경됨
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size) {
        noticeService.deleteNotice(id);
        return "redirect:/admin/dashboard";  // ✅ 변경됨
    }
}
