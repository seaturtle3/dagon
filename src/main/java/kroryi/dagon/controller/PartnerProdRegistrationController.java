package kroryi.dagon.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Data
@RequestMapping("partner")
public class PartnerProdRegistrationController {

    @GetMapping("prod-registration")
    public String partnerProdRegistration() {
        return "user/partnerProdRegistration";
    }
}
