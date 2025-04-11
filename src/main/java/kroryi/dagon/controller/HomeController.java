package kroryi.dagon.controller;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
public class HomeController {
    private final FishSpeciesRepository fishSpeciesRepository;

    public HomeController(FishSpeciesRepository fishSpeciesRepository) {
        this.fishSpeciesRepository = fishSpeciesRepository;
    }

    @ModelAttribute("regions")
    public ProdRegion[] regions() {
        return ProdRegion.values();
    }

    @ModelAttribute("fishSpecies")
    public List<ProductFishSpecies> fishSpecies() {
        return fishSpeciesRepository.findAll();
    }

    @GetMapping("/")
    public String home() {
        log.info("regions : {}", Arrays.toString(ProdRegion.values()));
        log.info("fishSpecies : {}", fishSpeciesRepository.findFsName());
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
