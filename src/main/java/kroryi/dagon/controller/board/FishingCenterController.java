package kroryi.dagon.controller.board;

import kroryi.dagon.entity.Product;
import kroryi.dagon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
public class FishingCenterController {

    private final ProductRepository productRepository;

    @GetMapping("fishing-center")
    public String fishingCenter(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "15") int size,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> products = productRepository.findAllWithNonEmptyReportOrDiary(pageable);

        model.addAttribute("products", products.getContent());
        model.addAttribute("productPage", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "board/fishing-center";
    }

}
