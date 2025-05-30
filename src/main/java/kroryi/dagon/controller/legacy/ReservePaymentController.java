package kroryi.dagon.controller.legacy;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservePaymentController {

    private final ProductService productService;
    @GetMapping("/{id}")
    public String showReservationForm(@PathVariable Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        log.info("showReservationForm--> {}",product);
        model.addAttribute("product", product);
        model.addAttribute("fishingAt", LocalDateTime.now());

        return "reservation/form"; // 템플릿 파일: reservation/form.html
    }

    @GetMapping("/confirm")
    public String confirmReservationPage() {
        return "reservation/confirm";
    }
}
