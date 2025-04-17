package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.multtae.*;
import kroryi.dagon.component.LunarApiClient;
import kroryi.dagon.component.MarineWeatherApiClient;
import kroryi.dagon.component.SunriseApiClient;
import kroryi.dagon.component.TideApiClient;
import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.multtae.TideStationRepository;
import kroryi.dagon.util.multtae.LunarUtil;
import kroryi.dagon.util.multtae.MarineDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multtae")
public class TideApiController {

    private final TideApiClient tideApiClient;
    private final MarineWeatherApiClient marineWeatherApiClient;
    private final TideStationRepository tideStationRepository;
    private final LunarApiClient lunarApiClient;
    private final SunriseApiClient sunriseApiClient;


    @GetMapping("/stations")
    @Operation(summary = "관측소 목록 조회")
    public List<Map<String, String>> getStationsByRegion(@RequestParam String region) {
        ProdRegion prodRegion = ProdRegion.fromKorean(region);
        return tideStationRepository.findByRegionOrderByStationNameAsc(prodRegion).stream()
                .map(station -> Map.of(
                        "stationCode", station.getStationCode(),
                        "stationName", station.getStationName()
                )).toList();
    }


    @GetMapping("/today")
    @Operation(summary = "오늘 정보 (물때, 월령, 일출일몰)")
    public MulttaeTodayDTO getTodayInfo(@RequestParam String stationCode) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        TideStation station = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));

        String location = station.getRegion().getKorean();

        List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, dateStr);
        List<WaveDTO> waveList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getWaveData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS);
        List<WindDTO> windList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getWindData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );
        List<AirTempDTO> airTempList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getAirTempData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );
        List<TideLevelDTO> tideLevelList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getTideLevelData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );

        Double lunAge = lunarApiClient.getLunarAge(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String mulName = LunarUtil.getMulName(lunAge);

        Map<String, String> sunMap = sunriseApiClient.getSunriseSunset(location, today);

        return MulttaeTodayDTO.builder()
                .date(today)
                .stationCode(stationCode)
                .stationName(station.getStationName())
                .mulName(mulName)
                .lunarAge(lunAge)
                .sunrise(sunMap.get("sunrise"))
                .sunset(sunMap.get("sunset"))
                .weatherNow("-")
                .temperature("-")
                .tideItems(tideItems)
                .waveList(waveList)
                .windList(windList)
                .airTempList(airTempList)
                .tideLevelList(tideLevelList)
                .build();
    }


    @GetMapping("/daily")
    @Operation(summary = "일간 물때 정보")
    public MulttaeDailyDTO getTideData(@RequestParam String stationCode,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String dateStr  = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, dateStr);
        List<WaveDTO> waveList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getWaveData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS);
        List<WindDTO> windList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getWindData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );
        List<AirTempDTO> airTempList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getAirTempData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );
        List<TideLevelDTO> tideLevelList = MarineDataUtil.filterByTargetHours(
                marineWeatherApiClient.getTideLevelData(stationCode, dateStr),
                MarineDataUtil.DEFAULT_HOURS
        );

        Double lunAge = lunarApiClient.getLunarAge(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        String mulName = lunAge != null ? LunarUtil.getMulName(lunAge) : null;

        return MulttaeDailyDTO.builder()
                .date(date)
                .lunarAge(lunAge)
                .mulName(mulName)
                .tideItems(tideItems)
                .waveList(waveList)
                .windList(windList)
                .airTempList(airTempList)
                .tideLevelList(tideLevelList)
                .build();
    }


    @GetMapping("/week")
    @Operation(summary = "주간 물때 정보")
    public List<MulttaeDailyDTO> getWeekTideData(@RequestParam String stationCode,
                                                 @RequestParam(defaultValue = "0") int offset) {
        LocalDate startDate = LocalDate.now().plusDays(offset * 7);
        List<MulttaeDailyDTO> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, dateStr);
            List<WaveDTO> waveList = MarineDataUtil.filterByTargetHours(
                    marineWeatherApiClient.getWaveData(stationCode, dateStr),
                    MarineDataUtil.DEFAULT_HOURS);
            List<WindDTO> windList = MarineDataUtil.filterByTargetHours(
                    marineWeatherApiClient.getWindData(stationCode, dateStr),
                    MarineDataUtil.DEFAULT_HOURS
            );
            List<AirTempDTO> airTempList = MarineDataUtil.filterByTargetHours(
                    marineWeatherApiClient.getAirTempData(stationCode, dateStr),
                    MarineDataUtil.DEFAULT_HOURS
            );
            List<TideLevelDTO> tideLevelList = MarineDataUtil.filterByTargetHours(
                    marineWeatherApiClient.getTideLevelData(stationCode, dateStr),
                    MarineDataUtil.DEFAULT_HOURS
            );

            Double lunAge = lunarApiClient.getLunarAge(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
            String mulName = lunAge != null ? LunarUtil.getMulName(lunAge) : null;

            result.add(MulttaeDailyDTO.builder()
                    .date(date)
                    .lunarAge(lunAge)
                    .mulName(mulName)
                    .tideItems(tideItems)
                    .waveList(waveList)
                    .windList(windList)
                    .airTempList(airTempList)
                    .tideLevelList(tideLevelList)
                    .build());
        }
        return result;
    }

}
