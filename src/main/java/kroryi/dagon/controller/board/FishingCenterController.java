package kroryi.dagon.controller.board;

import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class FishingCenterController {

    private final ProductRepository productRepository;

    @GetMapping("fishing-center")
    public String fishingCenter(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        log.info("products");
        return "board/fishing-center";
    }

    // 조황,조행기 페이지
    @GetMapping("/fishing-report/list/{id}")
    public String showFishingReport(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 제품 id=" + id));
        model.addAttribute("product", product);
        log.info("prodId : {}", product);
        return "board/fishingReport/list";
    }
}
