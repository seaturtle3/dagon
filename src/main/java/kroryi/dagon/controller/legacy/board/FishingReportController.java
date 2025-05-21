package kroryi.dagon.controller.legacy.board;

import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.service.board.fishingReportDiary.FishingReportService;
import kroryi.dagon.service.product.ProductService;
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

    // 조황 특정 prodId 조회
    @GetMapping("/list/{prodId}")
    public String getFishingReportsByProdId(@PathVariable Long prodId,
                                            Model model) {
        List<FishingReportDTO> fishingReports = fishingReportService.getFishingReportsByProdId(prodId);
        model.addAttribute("fishingReports", fishingReports);
        model.addAttribute("prodId", prodId);
        log.info("-------------------특정 prodId: {}", prodId);
        log.info("---------------------Fishing Reports: {}", fishingReports);  // 반환된 데이터 확인
        return "board/fishingReport/list";
    }


    // 조황 폼
    @GetMapping("/form")
    public String showFishingReportForm(@RequestParam(required = false) Long prodId,
                                        Model model) {
        if (prodId == null) {
            throw new IllegalArgumentException("prodId is required");
        }
        Product product = productService.findById(prodId);
        model.addAttribute("product", product);
        model.addAttribute("prodId", prodId);
        return "board/fishingReport/form";
    }

    // 조황 폼 전송
    @PostMapping("/form")
    public String createFishingReport(@RequestParam(required = false) Long prodId,
                                      FishingReportDTO fishingReportDTO) {
        Product product = productService.findById(prodId);
        fishingReportDTO.setProduct(product);
        fishingReportService.createFishingReport(fishingReportDTO);
        log.info("product id: {}", product);
        log.info("-----------------------Redirecting to /fishing-report/list/{}", prodId);
        return "redirect:/fishing-report/list/" + prodId;
    }

}

