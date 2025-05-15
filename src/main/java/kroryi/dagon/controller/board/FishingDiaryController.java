package kroryi.dagon.controller.board;

import kroryi.dagon.DTO.board.FishingReportDiary.FishingDiaryDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.service.board.fishingReportDiary.FishingDiaryService;
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
@RequestMapping("/fishing-diary")
public class FishingDiaryController {

    private final FishingDiaryService fishingDiaryService;
    private final ProductService productService;

    @GetMapping("/list/{prodId}")
    public String fishingDiaryList(@PathVariable Long prodId, Model model) {
        List<FishingDiaryDTO> fishingDiaries = fishingDiaryService.getFishingDiariesByProdId(prodId);
        model.addAttribute("fishingDiaries", fishingDiaries);
        return "board/fishingDiary/list";
    }

    @GetMapping("/form")
    public String showFishingDiaryForm(@RequestParam(required = false) Long prodId, Model model) {
        if (prodId == null) {
            throw new IllegalArgumentException("prodId is required");
        }
        Product product = productService.findById(prodId);
        model.addAttribute("product", product);
        model.addAttribute("prodId", prodId);
        return "board/fishingDiary/form";
    }

    @PostMapping("/form")
    public String createFishingDiary(@RequestParam Long prodId, Model model,
                                     FishingDiaryDTO fishingDiaryDTO) {
        Product product = productService.findById(prodId);
        fishingDiaryDTO.setProduct(product);
        fishingDiaryService.createFishingDiary(fishingDiaryDTO);
        model.addAttribute("product", product);
        log.info("form prodId---------------------{}",prodId);
        log.info("form product ---------------------{}",product);
        return "redirect:/fishing-diary/list/" + prodId;
    }

}
