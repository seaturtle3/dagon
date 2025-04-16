package kroryi.dagon.controller;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.multtae.AirTempDTO;
import kroryi.dagon.DTO.multtae.TideLevelDTO;
import kroryi.dagon.DTO.multtae.WaveDTO;
import kroryi.dagon.DTO.multtae.WindDTO;
import kroryi.dagon.component.MarineWeatherApiClient;
import kroryi.dagon.util.MarineDataUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/multtae/test")
public class MarineWeatherTestController {

    private final MarineWeatherApiClient marineWeatherApiClient;

    @GetMapping("/wave")
    @Operation(summary = "파고 정보 테스트")
    public List<WaveDTO> testWave(@RequestParam String stationCode,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<WaveDTO> allData = marineWeatherApiClient.getWaveData(stationCode, date.format(DateTimeFormatter.BASIC_ISO_DATE));

        return MarineDataUtil.filterByTargetHours(allData, MarineDataUtil.DEFAULT_HOURS);
    }

    @GetMapping("/temp")
    @Operation(summary = "기온 정보 테스트")
    public List<AirTempDTO> testTemp(@RequestParam String stationCode,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AirTempDTO> allData = marineWeatherApiClient.getAirTempData(stationCode, date.format(DateTimeFormatter.BASIC_ISO_DATE));
        return MarineDataUtil.filterByTargetHours(allData, MarineDataUtil.DEFAULT_HOURS);
    }

    @GetMapping("/wind")
    @Operation(summary = "풍향/풍속 정보 테스트")
    public List<WindDTO> testWind(@RequestParam String stationCode,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<WindDTO> allData = marineWeatherApiClient.getWindData(stationCode, date.format(DateTimeFormatter.BASIC_ISO_DATE));
        return MarineDataUtil.filterByTargetHours(allData, MarineDataUtil.DEFAULT_HOURS);
    }

    @GetMapping("/tide-level")
    @Operation(summary = "예측 조위 정보 테스트")
    public List<TideLevelDTO> testTideLevel(@RequestParam String stationCode,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TideLevelDTO> allData = marineWeatherApiClient.getTideLevelData(stationCode, date.format(DateTimeFormatter.BASIC_ISO_DATE));
        return MarineDataUtil.filterByTargetHours(allData, MarineDataUtil.DEFAULT_HOURS);
    }
}