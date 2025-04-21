package kroryi.dagon.controller;

import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.FishSpeciesRepository;
import kroryi.dagon.service.SeaFreshwaterFishingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/fishing")
public class SeaFreshwaterFishingController {

    private final SeaFreshwaterFishingService seaFreshwaterFishingService;
    private final FishSpeciesRepository fishSpeciesRepository;

    // 공통 데이터 주입
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

    private SubType convertToSubType(String subType) {
        if (subType == null || subType.equals("전체")) {
            return null;
        }

        for (SubType type : SubType.values()) {
            if (type.getKorean().equals(subType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown subType: " + subType);
    }


    // 공통 메서드로 파라미터 바인딩
    private void addSearchAttributes(LocalDate date, Integer people, String region, String mainType, String subType,
                                     Model model) {
        model.addAttribute("mainType", mainType);
        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
        model.addAttribute("subType", subType);
    }

    @GetMapping("/sea")
    public String sea(
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String mainType,
            @RequestParam(required = false) String subType,
            @RequestParam(required = false) String fishType,
            HttpServletRequest request,
            Model model) {

        ProdRegion convertedProdRegion = convertToProdRegion(region);
        MainType convertedMainType = convertToMainType(mainType);
        SubType convertedSubType = convertToSubType(subType);

        if (convertedMainType == null) {
            // URL이 /fishing/sea라면 SEA, /fishing/freshwater라면 FRESHWATER로 강제 지정
            if (request.getRequestURI().contains("/sea")) {
                convertedMainType = MainType.SEA;
            } else if (request.getRequestURI().contains("/freshwater")) {
                convertedMainType = MainType.FRESHWATER;
            }
        }

        addSearchAttributes(date, people, region, mainType, subType, model);
        List<ProductDTO> products = seaFreshwaterFishingService.getProductsByFilters(
                date != null ? date : null, convertedProdRegion, convertedMainType, convertedSubType, fishType);

        log.info("Sea Fetched Products: {}", products);
        log.info("Sea Filters applied - Region: {}, MainType: {}, SubType: {}, FishType: {}, AvailableDate: {}",
                region, mainType, subType, fishType, date);

        model.addAttribute("products", products);
        return "menu/sea_fishing";
    }

    @GetMapping("/freshwater")
    public String freshwater(
            @RequestParam(required = false) String mainType,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String subType,  // subType 파라미터 추가
            @RequestParam(required = false) String fishType,
            HttpServletRequest request,
            Model model) {

        ProdRegion convertedProdRegion = convertToProdRegion(region);
        MainType convertedMainType = convertToMainType(mainType);
        SubType convertedSubType = convertToSubType(subType);  // subType 변환

        if (convertedMainType == null) {
            if (request.getRequestURI().contains("/sea")) {
                convertedMainType = MainType.SEA;
            } else if (request.getRequestURI().contains("/freshwater")) {
                convertedMainType = MainType.FRESHWATER;
            }
        }

        addSearchAttributes(date, people, region, mainType, subType, model);  // 수정된 메서드 호출
        List<ProductDTO> products = seaFreshwaterFishingService.getProductsByFilters(
                date != null ? date : null, convertedProdRegion, convertedMainType, convertedSubType, fishType);  // subType도 추가

        log.info("Freshwater Fetched Products: {}", products);
        log.info("Freshwater Filters applied - Region: {}, MainType: {}, SubType: {}, FishType: {}, AvailableDate: {}",
                region, mainType, subType, fishType, date);

        model.addAttribute("products", products);
        return "menu/freshwater_fishing";
    }

}
