package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FishingReportDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.service.FishingReportService;
import kroryi.dagon.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/fishing-report")
public class FishingReportController {

    private final FishingReportService fishingReportService;
    private final ProductService productService;
    private final ProductRepository productRepository;

    // 모든 조황 정보 조회
    @GetMapping("/list")
    public String getAllFishingReports(Model model) {
        List<FishingReportDTO> fishingReports = fishingReportService.getAllFishingReport();
        model.addAttribute("fishingReports", fishingReports);
        return "board/fishingReport/list";
    }

    // 조황 등록 페이지
    @GetMapping("/form")
    public String showFishingReportForm(@RequestParam(required = false) Long prodId, Model model) {
        if (prodId == null) {
            // prodId가 없으면 적절한 처리를 추가 (예: 에러 페이지나 리다이렉트)
            return "redirect:/fishing-report/list";
        }
        Product product = productService.findById(prodId);
        model.addAttribute("product", product);
        return "board/fishingReport/form";
    }

    // 조황 등록 전송
    @PostMapping("/form")
    public String createFishingReport(@RequestParam(required = false) Long prodId, Model model,
                                      FishingReportDTO fishingReportDTO) {
        if (prodId == null) {
            // prodId가 없으면 적절한 처리를 추가 (예: 에러 페이지나 리다이렉트)
            return "redirect:/fishing-report/list";
        }
        Product product = productService.findById(prodId);
        model.addAttribute("product", product);

        fishingReportService.createFishingReport(fishingReportDTO);
        return "redirect:/fishing-report/list"; // 등록 후 목록 페이지로 리다이렉트
    }

}

