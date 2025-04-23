package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.service.FishingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fishing-report")
public class FishingReportController {

    private final FishingReportService fishingReportService;

    // 모든 조황 정보 조회
    @GetMapping("/list")
    public String getAllFishingReports(Model model) {
        List<FishingReportDTO> fishingReports = fishingReportService.getAllFishingReport();
        model.addAttribute("fishingReports", fishingReports);
        return "board/fishReport/list";
    }

    // 새로운 조황 정보 등록
    @GetMapping("/form")
    public String showFishingReportForm() {
        return "board/fishReport/form";
    }

    // 새로운 조황 정보 등록
    @PostMapping("/form")
    public String createFishingReport(FishingReportDTO fishingReportDTO) {
        fishingReportService.createFishingReport(fishingReportDTO);
        return "redirect:/fishing-report/list"; // 등록 후 목록 페이지로 리다이렉트
    }
}

