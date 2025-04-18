package kroryi.dagon.controller;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
import kroryi.dagon.service.ProductService;
import kroryi.dagon.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final FishSpeciesRepository fishSpeciesRepository;
    private final ProductService productService;






    // 공통 데이터 주입
    @ModelAttribute("regions")
    public ProdRegion[] regions() {
        return ProdRegion.values();
    }

    @ModelAttribute("fishSpecies")
    public List<ProductFishSpecies> fishSpecies() {
        return fishSpeciesRepository.findAll();
    }

    private ProdRegion convertToProdRegion(String region) {
        if(region == null || region.equals("전체")) {
            return null;
        }

        for(ProdRegion prodRegion : ProdRegion.values()) {
            if (prodRegion.getKorean().equals(region)) {
                return prodRegion;
            }
        }
        throw new IllegalArgumentException("Unknown region: " + region);
    }

    // 공통 메서드로 파라미터 바인딩
    private void addSearchAttributes(Model model, String type,
                                     String date, Integer people,
                                     String region, String fishType) {
        model.addAttribute("type", type);
        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
        model.addAttribute("fishType", fishType);
        log.info("Search Params => type: {}, date: {}, people: {}, region: {}, fishType: {}", type, date, people, region, fishType);
    }

    @GetMapping("/sea")
    public String sea(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String fishType,
            Model model) {

        ProdRegion prodRegion = convertToProdRegion(region);

        addSearchAttributes(model, type, date, people, region, fishType);

        List<ProductDTO> products = reservationService.getAllProductsByRegion(prodRegion);
        model.addAttribute("products", products);

        return "menu/sea_fishing";
    }

    @GetMapping("/freshwater")
    public String freshwater(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String fishType,
            Model model) {

        addSearchAttributes(model, type, date, people, region, fishType);
        return "menu/freshwater_fishing";
    }


}
