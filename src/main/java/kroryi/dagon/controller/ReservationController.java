package kroryi.dagon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "예약 페이지 진입",
            description = "날짜, 인원, 지역, 어종 정보를 전달하여 예약 페이지에 진입합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "정상 처리됨")
            }
    )
    @GetMapping("/reservation")
    public String reservation(
            @Parameter(description = "예약 날짜 (yyyy-MM-dd)") @RequestParam(required = false) String date,
            @Parameter(description = "예약 인원 수") @RequestParam(required = false) Integer people,
            @Parameter(description = "지역 코드") @RequestParam(required = false) String region,
            @Parameter(description = "어종 코드") @RequestParam(required = false) String fishType,
            Model model) {

        model.addAttribute("date", date);
        model.addAttribute("people", people);
        model.addAttribute("region", region);
        model.addAttribute("fishType", fishType);

        log.info("date : {}, people : {}, region : {}, fishType : {}", date , people , region, fishType);

        return "sub_menu/reservation";
    }
}
