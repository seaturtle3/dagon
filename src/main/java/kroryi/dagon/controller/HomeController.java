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
        return "menu/fishing_report";
    }

    @GetMapping("/sea_fishing")
    public String menu2() {
        return "menu/sea_fishing";
    }

    @GetMapping("/freshwater_fishing")
    public String menu3() {
        return "menu/freshwater_fishing";
    }

    @GetMapping("/community")
    public String menu4() {
        return "menu/community";
    }

    @GetMapping("/cs_center")
    public String menu5() {
        return "menu/cs_center";
    }

    @GetMapping("/my_page")
    public String myPage() {
        return "user/my_page";
    }

    @GetMapping("/notice")
    public String notice() {
        return "sub_menu/notice";
    }



}
