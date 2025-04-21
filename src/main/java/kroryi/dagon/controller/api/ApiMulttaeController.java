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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multtae")
public class ApiMulttaeController {
    private final TideApiClient tideApiClient;
    private final MarineWeatherApiClient marineWeatherApiClient;
    private final TideStationRepository tideStationRepository;
    private final LunarApiClient lunarApiClient;
    private final SunriseApiClient sunriseApiClient;

    @GetMapping("/stations")
    @Operation(summary = "지역별 관측소 목록 조회")
    public List<Map<String, String>> getStationsByRegion(@RequestParam String region) {
        ProdRegion prodRegion = ProdRegion.fromKorean(region);
        return tideStationRepository.findByRegionOrderByStationNameAsc(prodRegion).stream()
                .map(station -> Map.of(
                        "stationCode", station.getStationCode(),
                        "stationName", station.getStationName()
                ))
                .toList();
    }

    @GetMapping("/today")
    public MulttaeDailyDTO getTodayInfo(@RequestParam String stationCode) {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // ✅ 1. 관측소 정보 조회
        TideStation station = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));
        log.info("📍관측소 조회됨: {} - {}", station.getStationCode(), station.getStationName());

        // ✅ 2. 물때(조석) 정보 조회
        List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, dateStr);
        log.info("🌊 TideItems: {}", tideItems);

        // ✅ 3. 기상 API 데이터 조회 및 머지 (→ HourlyDataDTO 생성)
        List<WaveDTO> waveList = marineWeatherApiClient.getWaveData(stationCode, dateStr);
        List<WindDTO> windList = marineWeatherApiClient.getWindData(stationCode, dateStr);
        List<AirTempDTO> airTempList = marineWeatherApiClient.getAirTempData(stationCode, dateStr);
        List<TideLevelDTO> tideLevelList = marineWeatherApiClient.getTideLevelData(stationCode, dateStr);

        log.info("🌊 Wave: {}", waveList);
        log.info("🍃 Wind: {}", windList);
        log.info("🌡️ AirTemp: {}", airTempList);
        log.info("📈 TideLevel: {}", tideLevelList);

        // ✅ 4. 각 기상 데이터를 시간대별로 병합
        List<HourlyDataDTO> hourlyData = MarineDataMapper.mergeToHourly(
                waveList, windList, airTempList, tideLevelList
        );
        log.info("🕒 HourlyDataDTO: {}", hourlyData);

        // ✅ 5. 월령 → 물 이름 계산
        Double lunarAge = lunarApiClient.getLunarAge(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String mulName = LunarUtil.getMulName(lunarAge);

        // ✅ 6. 일출/일몰 정보 조회
        Map<String, String> sunMap = sunriseApiClient.getSunriseSunset(station.getRegion().getKorean(), today);

        // ✅ 7. 최종 응답 DTO 조립
        return MulttaeDailyDTO.builder()
                .date(today)
                .stationCode(stationCode)
                .stationName(station.getStationName())
                .sunrise(sunMap.get("sunrise"))
                .sunset(sunMap.get("sunset"))
                .lunarAge(lunarAge)
                .mulName(mulName)
                .tideItems(tideItems)
                .hourlyData(hourlyData)
                .build();
    }


}