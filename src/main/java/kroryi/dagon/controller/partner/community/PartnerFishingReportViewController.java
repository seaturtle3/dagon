package kroryi.dagon.controller.partner.community;

import kroryi.dagon.DTO.board.FishingReportDiary.FishingReportDTO;
import kroryi.dagon.entity.FishingReport;
import kroryi.dagon.entity.Product;
import kroryi.dagon.service.community.fishingReportDiary.FishingReportService;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/fishing-report")
public class PartnerFishingReportViewController {

    private final FishingReportService fishingReportService;
    private final ProductService productService;

    // 조황정보 상품ID로 조회
    @GetMapping("/list/{prodId}")
    public String getFishingReportsByProdId(@PathVariable Long prodId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            Model model) {
        Page<FishingReportDTO> reportPage = fishingReportService.getFishingReportsByProdId(prodId, page, size);

        model.addAttribute("reportPage", reportPage);
        model.addAttribute("fishingReports", reportPage.getContent()); // 기존 코드 호환용
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reportPage.getTotalPages());
        model.addAttribute("prodId", prodId);

        return "board/fishing-center/report/list";
    }

    // 조황정보 폼
    @GetMapping("/form")
    public String showFishingReportForm(@RequestParam(required = false) Long prodId,
                                        @RequestParam(required = false) Long id,
                                        Model model) {
        if (prodId == null) {
            throw new IllegalArgumentException("prodId is required");
        }
        Product product = productService.findById(prodId);
        model.addAttribute("product", product);
        model.addAttribute("prodId", prodId);

        if (id != null) {
            FishingReportDTO reportDTO = fishingReportService.findDTOById(id);
            model.addAttribute("report", reportDTO);
        }

        log.info("----------------------- prodId:{}, id:{}", prodId, id);

        return "board/fishing-center/report/form";
    }

    // 조황정보 폼 전송
    @PostMapping("/form")
    public String createOrUpdateFishingReport(@RequestParam(required = false) Long prodId,
                                              @RequestParam(required = false) Long id,
                                              FishingReportDTO fishingReportDTO) {

        Product product = productService.findById(prodId);
        fishingReportDTO.setProduct(product);

        Long savedId;
        if (id != null) {
            savedId = fishingReportService.updateFishingReport(id, fishingReportDTO);
            return "redirect:/fishing-report/detail/" + savedId;
        } else {
            fishingReportService.createFishingReport(fishingReportDTO);
            return "redirect:/fishing-report/list/" + prodId;
        }
    }

    @GetMapping("/detail/{id}")
    public String showReportDetail(@PathVariable Long id,
                                   Model model) {
        FishingReport report = fishingReportService.findById(id);
        model.addAttribute("report", report);
        model.addAttribute("prodId", report.getProduct().getProdId());

        return "board/fishing-center/report/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteReportDetail(@PathVariable Long id) {
        Long prodId = fishingReportService.deleteAndReturnProdId(id);
        return "redirect:/fishing-center/report/list/" + prodId;
    }

}