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
import kroryi.dagon.service.multtae.MulttaeAsyncService;
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
import java.util.concurrent.CompletableFuture;

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
    private final MulttaeAsyncService asyncService;

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

        // 관측소 정보 조회
        TideStation station = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));

        // 기상 정보 병렬 조회
        CompletableFuture<List<WaveDTO>> waveFuture = asyncService.getWaveDataAsync(station, dateStr);
        CompletableFuture<List<WindDTO>> windFuture = asyncService.getWindDataAsync(stationCode, dateStr);
        CompletableFuture<List<AirTempDTO>> airFuture = asyncService.getAirDataAsync(stationCode, dateStr);
        CompletableFuture<List<TideLevelDTO>> tideLevelFuture = asyncService.getTideDataAsync(stationCode, dateStr);

        CompletableFuture.allOf(waveFuture, windFuture, airFuture, tideLevelFuture).join();

        // 시간대별 기상 데이터 병합
        List<HourlyDataDTO> hourlyData = MarineDataMapper.mergeToHourly(
                waveFuture.join(), windFuture.join(), airFuture.join(), tideLevelFuture.join()
        );

        // 물때 정보 조회
        List<TideItemDTO> tideItems = tideApiClient.getTideItems(stationCode, dateStr);

        // 월령 → 물 이름 계산
        Double lunarAge = lunarApiClient.getLunarAge(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
        String mulName = LunarUtil.getMulName(lunarAge);

        // 일출/일몰 정보 조회
        Map<String, String> sunMap = sunriseApiClient.getSunriseSunset(station.getRegion().getKorean(), today);

        // 최종 DTO 조립
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