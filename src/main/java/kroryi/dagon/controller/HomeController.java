package kroryi.dagon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/fishing_report")
    public String menu1() {
        return "fishing_report";
    }

    @GetMapping("/sea_fishing")
    public String menu2() {
        return "sea_fishing";
    }

    @GetMapping("/freshwater_fishing")
    public String menu3() {
        return "freshwater_fishing";
    }

    @GetMapping("/community")
    public String menu4() {
        return "community";
    }

    @GetMapping("/cs_center")
    public String menu5() {
        return "cs_center";
    }

    @GetMapping("/my_page")
    public String myPage() {
        return "my_page";
    }
}
