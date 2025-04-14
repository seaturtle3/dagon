package kroryi.dagon.controller;

import kroryi.dagon.DTO.TideItemDTO;
import kroryi.dagon.component.TideApiClient;
import kroryi.dagon.entity.TideStation;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.TideStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tide")
public class TideController {

    private final TideApiClient tideApiClient;
    private final TideStationRepository tideStationRepository;


    @GetMapping("/multtae")
    public String showMulttaePage(@RequestParam(required = false) String region,
                                  @RequestParam(required = false) String stationCode,
                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  Model model) {
        model.addAttribute("regions", ProdRegion.values());
        // 기본값 설정
        if (region == null) region = "인천";
        if (stationCode == null) stationCode = "DT_0001";
        if (date == null) date = LocalDate.now();


        // 지역 그룹핑 (Map<ProdRegion, List<TideStation>>)
        Map<ProdRegion, List<TideStation>> groupedStations = Arrays.stream(ProdRegion.values())
                .collect(Collectors.toMap(
                        r -> r,
                        r -> tideStationRepository.findByRegion(r)
                ));

        model.addAttribute("groupedStations", groupedStations);
        model.addAttribute("region", region);
        model.addAttribute("stationCode", stationCode);
        model.addAttribute("date", date);

        return "menu/multtae";
    }


}
