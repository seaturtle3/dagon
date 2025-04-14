package kroryi.dagon.controller;

import kroryi.dagon.entity.ProdFishSpeciesMapping;
import kroryi.dagon.entity.ProductFishSpecies;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.FishSpeciesRepository;
import kroryi.dagon.service.ReservationService;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@Getter
@Setter
@ToString
@AllArgsConstructor
@Log4j2
public class ReservationController {

    private final ReservationService reservationService;
    private final FishSpeciesRepository fishSpeciesRepository;

    @ModelAttribute("regions")
    public List<ProdRegion> regions() {
        return Arrays.asList(ProdRegion.values());
    }

    @ModelAttribute("fishSpecies")
    public List<ProductFishSpecies> fishSpecies() {
        return fishSpeciesRepository.findAll();
    }

    @GetMapping("/reservation")
    public String reservation(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer people,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String fishType,
            Model model) {

        // 파라미터 값을 모델에 담아서 reservation.html에서 사용 가능하게 함
        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
        model.addAttribute("fishType", fishType);

        log.info("date : {}, people : {}, region : {}, fishType : {}", date , people , region, fishType);

        // reservation.html 반환
        return "sub_menu/reservation";
    }


}
