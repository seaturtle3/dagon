package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.MulttaeDailyDTO;
import kroryi.dagon.DTO.TideItemDTO;
import kroryi.dagon.component.LunarApiClient;
import kroryi.dagon.component.TideApiClient;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.TideStationRepository;
import kroryi.dagon.util.LunarUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multtae")
public class TideApiController {

    private final TideApiClient tideApiClient;
    private final TideStationRepository tideStationRepository;
    private final LunarApiClient lunarApiClient;


    @GetMapping("/stations")
    public List<Map<String, String>> getStationsByRegion(@RequestParam String region) {
        ProdRegion prodRegion = ProdRegion.fromKorean(region);
        return tideStationRepository.findByRegionOrderByStationNameAsc(prodRegion).stream()
                .map(station -> Map.of(
                        "stationCode", station.getStationCode(),
                        "stationName", station.getStationName()
                )).toList();
    }

    @GetMapping("/daily")
    public MulttaeDailyDTO getTideData(@RequestParam String stationCode,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, formattedDate);

        Double lunAge = lunarApiClient.getLunarAge(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        String mulName = lunAge != null ? LunarUtil.getMulName(lunAge) : null;

        return MulttaeDailyDTO.builder()
                .date(date)
                .lunarAge(lunAge)
                .mulName(mulName)
                .tideItems(tideItems)
                .build();
    }

    @GetMapping("/week")
    public List<MulttaeDailyDTO> getWeekTideData(
            @RequestParam String stationCode,
            @RequestParam(defaultValue = "0") int offset
    ) {
        LocalDate startDate = LocalDate.now().plusDays(offset * 7);
        List<MulttaeDailyDTO> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, formattedDate);
            Double lunAge = lunarApiClient.getLunarAge(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            String mulName = lunAge != null ? LunarUtil.getMulName(lunAge) : null;

            result.add(MulttaeDailyDTO.builder()
                    .date(date)
                    .lunarAge(lunAge)
                    .mulName(mulName)
                    .tideItems(tideItems)
                    .build());
        }

        return result;
    }


    @GetMapping("/lunar")
    public Map<String, Object> getLunarInfo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Double lunAge = lunarApiClient.getLunarAge(
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());

        Map<String, Object> response = new HashMap<>();
        response.put("solDate", date.toString());

        if (lunAge != null) {
            String mulName = LunarUtil.getMulName(lunAge);
            response.put("lunarAge", lunAge);
            response.put("mulName", mulName);
        } else {
            response.put("error", "월령 정보를 가져올 수 없습니다.");
        }

        return response;
    }


}
