package kroryi.dagon.controller.legacy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentViewController {

    @GetMapping("/account")
    public String showAccountPage() {
        return "payments/account";
    }
}
