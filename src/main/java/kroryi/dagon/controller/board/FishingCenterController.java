package kroryi.dagon.controller.board;

import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

}
