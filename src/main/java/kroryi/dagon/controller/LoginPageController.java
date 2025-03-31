package kroryi.dagon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginPageController {

    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password) {
        return "login";
    }
}
