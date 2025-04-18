package kroryi.dagon.controller;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
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
        if (region == null || region.equals("전체")) {
            return null;
        }

        for (ProdRegion prodRegion : ProdRegion.values()) {
            if (prodRegion.getKorean().equals(region)) {
                return prodRegion;
            }
        }
        throw new IllegalArgumentException("Unknown region: " + region);
    }

    private MainType convertToMainType(String mainType) {
        if (mainType == null || mainType.equals("")) {
            return null;
        }

        try {
            return MainType.valueOf(mainType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown MainType: " + mainType);
        }
    }

    // 공통 메서드로 파라미터 바인딩
    private void addSearchAttributes(String mainType, String date, Integer people, String region, String fishType,
    Model model) {
        model.addAttribute("mainType", mainType);
        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
        model.addAttribute("fishType", fishType);

        log.info("Search Params => mainType: {}, date: {}, people: {}, region: {}, fishType: {}",
                mainType, date, people, region, fishType);
    }

    @GetMapping("/sea")
    public String sea(
            @RequestParam(required = false) String mainType,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String fishType,
            Model model) {

        ProdRegion convertedProdRegion = convertToProdRegion(region);
        MainType convertedMainType = convertToMainType(mainType);

        addSearchAttributes(mainType, date, people, region, fishType, model);
        List<ProductDTO> products = reservationService.getAllProductsByRegionAndMainType(convertedProdRegion, convertedMainType);
        model.addAttribute("products", products);

        log.info("----------sea getAllProductsByRegionAndMainType {}", products);

        return "menu/sea_fishing";
    }

    @GetMapping("/freshwater")
    public String freshwater(
            @RequestParam(required = false) String mainType,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String fishType,
            Model model) {

        ProdRegion convertedProdRegion = convertToProdRegion(region);
        MainType convertedMainType = convertToMainType(mainType);

        addSearchAttributes(mainType, date, people, region, fishType, model);
        List<ProductDTO> products = reservationService.getAllProductsByRegionAndMainType(convertedProdRegion, convertedMainType);
        model.addAttribute("products", products);

        log.info("----------freshWater getAllProductsByRegionAndMainType {}", convertedMainType.getKorean());

        return "menu/freshwater_fishing";
    }


}
