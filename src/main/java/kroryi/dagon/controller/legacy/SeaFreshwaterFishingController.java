package kroryi.dagon.controller.legacy;

import jakarta.servlet.http.HttpServletRequest;
import kroryi.dagon.entity.Product;
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
        if (mainType == null || mainType.equals("전체")) {
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
        try {
            return SubType.valueOf(subType);  // 영어 enum name으로 변환
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown subType: " + subType);
        }
    }


    // 공통 메서드로 파라미터 바인딩
    private void addSearchAttributes(LocalDate date, Integer people, String region, String mainType, String subType,
                                     Model model) {
        model.addAttribute("mainType", mainType);
        model.addAttribute("subType", subType);
        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
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

        // URL에 따른 MainType 설정
        if (convertedMainType == null) {
            if (request.getRequestURI().contains("/sea")) {
                convertedMainType = MainType.SEA;
            } else if (request.getRequestURI().contains("/freshwater")) {
                convertedMainType = MainType.FRESHWATER;
            }
        }

        SubType convertedSubType = null;
        if (subType != null && !subType.equals("전체")) {
            convertedSubType = convertToSubType(subType);
        }

        // 필터링된 상품 목록을 모델에 추가
        List<Product> products = seaFreshwaterFishingService.getProductsByFilters(
                convertedMainType, convertedSubType, convertedProdRegion
        );
        model.addAttribute("products", products);

        log.info("Fetched Products: {}", products);
        log.info("Filtering Products with Region: {}, MainType: {}, SubType: {}", region, mainType, subType);

        return "fishing/sea";
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

        // URL에 따른 MainType 설정
        if (convertedMainType == null) {
            if (request.getRequestURI().contains("/sea")) {
                convertedMainType = MainType.SEA;
            } else if (request.getRequestURI().contains("/freshwater")) {
                convertedMainType = MainType.FRESHWATER;
            }
        }

        SubType convertedSubType = null;
        if (subType != null && !subType.equals("전체")) {
            convertedSubType = convertToSubType(subType);
        }

        // 필터링된 상품 목록을 모델에 추가
        List<Product> products = seaFreshwaterFishingService.getProductsByFilters(
                convertedMainType, convertedSubType, convertedProdRegion
        );
        model.addAttribute("products", products);

        return "fishing/freshwater";
    }

}
