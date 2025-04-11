package kroryi.dagon.controller;

import kroryi.dagon.enums.ProdRegion;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
@Log4j2
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("regions", ProdRegion.values());

        log.info("regions : {}", Arrays.toString(ProdRegion.values()));

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
