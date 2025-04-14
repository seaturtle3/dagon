package kroryi.dagon.controller;

import kroryi.dagon.DTO.TideItemDTO;
import kroryi.dagon.component.TideApiClient;
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
import java.util.List;

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

        if (region != null) {
            ProdRegion prodRegion = ProdRegion.fromKorean(region);
            model.addAttribute("stations", tideStationRepository.findByRegion(prodRegion));
        }

      if(stationCode != null && date != null) {
          String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, formattedDate);
          model.addAttribute("tideItems", tideItems);
          model.addAttribute("stationCode", stationCode);
          model.addAttribute("date",date);
      }

      return "menu/multtae";
    }


}
