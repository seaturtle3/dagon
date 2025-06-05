package kroryi.dagon.controller.legacy;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Log4j2
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentsController {

    @GetMapping
    public String payments() {
        return "payments/payments";
    }

    @GetMapping("/payment")
    public String paymentPage(Model model) {
        model.addAttribute("productName", "테스트 상품");
        model.addAttribute("amount", 100);
        model.addAttribute("buyerEmail", "test@test.com");
        model.addAttribute("buyerName", "홍길동");
        model.addAttribute("buyerTel", "010-0000-0000");
        model.addAttribute("buyerAddr", "서울시 강남구");
        model.addAttribute("buyerPostcode", "12345");
        return "payments/payment";
    }
}
