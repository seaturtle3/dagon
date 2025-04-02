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
        return "fishing_report";
    }

    @GetMapping("/menu2")
    public String menu2() {
        return "sea_fishing";
    }

    @GetMapping("/menu3")
    public String menu3() {
        return "freshwater_fishing";
    }

    @GetMapping("/menu4")
    public String menu4() {
        return "community";
    }

    @GetMapping("/menu5")
    public String menu5() {
        return "cs_center";
    }

    @GetMapping("/my_page")
    public String myPage() {
        return "my_page";
    }
}
