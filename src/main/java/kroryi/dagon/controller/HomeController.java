package kroryi.dagon.controller;

import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.FishSpeciesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
public class HomeController {
    private final FishSpeciesRepository fishSpeciesRepository;

    @GetMapping("/community")
    public String community() {
        return "board/community";
    }

    @GetMapping("/cs-center")
    public String csCenter() {
        return "board/cs-center";
    }

    @GetMapping("/my-page")
    public String myPage() {

        log.info("homecontr----->/my-page");
        return "user/my-page";
    }

    // main.html에 지역, 어종, 서브타입 전달

    @ModelAttribute("regions")
    public ProdRegion[] regions() {
        return ProdRegion.values();
    }

    @ModelAttribute("fishSpecies")
    public List<ProductFishSpecies> fishSpecies() {
        return fishSpeciesRepository.findAll();
    }

    @ModelAttribute("subTypes")
    public SubType[] subTypes() {
        return SubType.values();
    }

    @GetMapping("/")
    public String home() {
        log.info("home--------------------------------");
        return "index";
    }


}
