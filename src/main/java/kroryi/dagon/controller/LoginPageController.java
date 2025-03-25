package kroryi.dagon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginPageController {

    @PostMapping("/login")
    public String login() {
        return "login";
    }
}
