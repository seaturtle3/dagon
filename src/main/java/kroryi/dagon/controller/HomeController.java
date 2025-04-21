package kroryi.dagon.controller;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class HomeController {
    private final FishSpeciesRepository fishSpeciesRepository;

    @GetMapping("/fishing_report")
    public String fishingReport() {
        return "menu/fishing_report";
    }

    @GetMapping("/community")
    public String community() {
        return "menu/community";
    }

    @GetMapping("/cs_center")
    public String csCenter() {
        return "menu/cs_center";
    }

    @GetMapping("/my_page")
    public String myPage() {
        return "user/my_page";
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


}
