package kroryi.dagon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/menu1")
    public String menu1() {
        return "menu/menu1";
    }

    @GetMapping("/menu2")
    public String menu2() {
        return "menu/menu2";
    }

    @GetMapping("/menu3")
    public String menu3() {
        return "menu/menu3";
    }

    @GetMapping("/menu4")
    public String menu4() {
        return "menu/menu4";
    }

    @GetMapping("/menu5")
    public String menu5() {
        return "menu/menu5";
    }

    @GetMapping("/my_page")
    public String myPage() {
        return "my_page";
    }
}
