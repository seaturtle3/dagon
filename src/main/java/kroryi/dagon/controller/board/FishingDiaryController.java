package kroryi.dagon.controller.board;

import kroryi.dagon.service.FishingReportService;
import kroryi.dagon.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/fishing-diary")
public class FishingDiaryController {

    private final FishingReportService fishingReportService;
    private final ProductService productService;

    @GetMapping("/list/{prodId}")
    public String fishingDiaryList(Model model) {
        return "board/fishingDiary/list";
    }

    @GetMapping("/form")
    public String showFishingDiaryForm(Model model) {
        return "board/fishingDiary/form";
    }

    @PostMapping("/form")
    public String createFishingDiary(Model model) {
        return "redirect:/fishing-diary/list";
    }

}
