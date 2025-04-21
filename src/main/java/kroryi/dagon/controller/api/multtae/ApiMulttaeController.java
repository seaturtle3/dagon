package kroryi.dagon.controller.api.multtae;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.multtae.*;
import kroryi.dagon.component.LunarApiClient;
import kroryi.dagon.component.MarineWeatherApiClient;
import kroryi.dagon.component.SunriseApiClient;
import kroryi.dagon.component.TideApiClient;
import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.entity.multtae.WaveStation;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.repository.multtae.TideStationRepository;
import kroryi.dagon.service.multtae.LunarCacheService;
import kroryi.dagon.service.multtae.MulttaeAsyncService;
import kroryi.dagon.service.multtae.SunriseCacheService;
import kroryi.dagon.util.multtae.LunarUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

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
    private final LunarCacheService lunarCacheService;
    private final SunriseCacheService sunriseCacheService;

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

    @GetMapping("/stations/with-wave")
    @Operation(summary = "파고 관측소가 연결된 조위 관측소 목록 조회")
    public List<Map<String, String>> getTideStationsWithWaveStation() {
        return tideStationRepository.findAll().stream()
                .filter(ts -> ts.getWaveStation() != null)
                .map(ts -> Map.of(
                        "stationCode", ts.getStationCode(),
                        "stationName", ts.getStationName(),
                        "waveStationCode", ts.getWaveStation().getStationCode(),
                        "waveStationName", ts.getWaveStation().getStationName()
                ))
                .toList();
    }

    @GetMapping("/stations/{stationCode}/wave")
    @Operation(summary = "조위관측소와 연결된 파고관측소 확인")
    public Map<String, Object> getLinkedWaveStation(@PathVariable String stationCode) {
        TideStation tideStation = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tideStation", Map.of(
                "stationCode", tideStation.getStationCode(),
                "stationName", tideStation.getStationName(),
                "region", tideStation.getRegion().name()
        ));

        if (tideStation.getWaveStation() != null) {
            WaveStation wave = tideStation.getWaveStation();
            result.put("waveStation", Map.of(
                    "stationCode", wave.getStationCode(),
                    "stationName", wave.getStationName()
            ));
        } else {
            result.put("waveStation", null);
        }

        return result;
    }

    @GetMapping("/test-wave")
    @Operation(summary = "WaveStation 테스트용 파고 데이터 조회")
    public List<WaveDTO> testWave(
            @RequestParam String waveStationCode,
            @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date
    ) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return marineWeatherApiClient.getWaveData(waveStationCode, dateStr);
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
        Double lunarAge = lunarCacheService.getLunarAge(today);
        String mulName = LunarUtil.getMulName(lunarAge);

        // 일출/일몰 정보 조회
        Map<String, String> sunMap = sunriseCacheService.getSunInfo(station.getRegion().getKorean(), today);

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

    @GetMapping("/daily")
    @Operation(summary = "하루 요약 정보 조회")
    public MulttaeDailySimpleDTO getDailySummary(
            @RequestParam String stationCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 관측소 조회
        TideStation station = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));

        // 비동기 풍속/풍향만 조회
        CompletableFuture<List<WindDTO>> windFuture = asyncService.getWindDataAsync(stationCode, dateStr);
        Map<String, String> sunMap = sunriseCacheService.getSunInfo(station.getRegion().getKorean(), date);
        Double lunarAge = lunarCacheService.getLunarAge(date);
        String mulName = LunarUtil.getMulName(lunarAge);

        // 9시 기준 풍속/풍향 추출
        List<WindDTO> windList = windFuture.join();
        WindDTO wind = MarineDataMapper.findClosest(windList, LocalTime.of(9, 0)); // 9시 기준

        return MulttaeDailySimpleDTO.builder()
                .date(date)
                .sunrise(sunMap.get("sunrise"))
                .sunset(sunMap.get("sunset"))
                .mulName(mulName)
                .windDir(wind != null ? wind.getWind_dir() : null)
                .windSpeed(wind != null ? wind.getWind_speed() : null)
                .build();
    }

    @GetMapping("/week")
    @Operation(summary = "주간 요약 정보 조회")
    public List<MulttaeDailySimpleDTO> getWeekData(@RequestParam String stationCode) {
        TideStation station = tideStationRepository.findById(stationCode)
                .orElseThrow(() -> new RuntimeException("관측소 없음"));

        return IntStream.range(0, 7)
                .mapToObj(i -> LocalDate.now().plusDays(i))
                .map(date -> {
                    String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    List<WindDTO> wind = marineWeatherApiClient.getWindData(stationCode, dateStr);
                    WindDTO w = MarineDataMapper.findClosest(wind, LocalTime.of(9, 0));

                    Double lunarAge = lunarCacheService.getLunarAge(date);
                    String mulName = LunarUtil.getMulName(lunarAge);
                    Map<String, String> sun = sunriseCacheService.getSunInfo(station.getRegion().getKorean(), date);

                    return MulttaeDailySimpleDTO.builder()
                            .date(date)
                            .mulName(mulName)
                            .sunrise(sun.getOrDefault("sunrise", "-"))
                            .sunset(sun.getOrDefault("sunset", "-"))
                            .windDir(w != null ? w.getWind_dir() : null)
                            .windSpeed(w != null ? w.getWind_speed() : null)
                            .build();
                })
                .toList();
    }
}