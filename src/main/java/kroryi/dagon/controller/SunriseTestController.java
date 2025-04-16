package kroryi.dagon.controller;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.component.SunriseApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/multtae")
public class SunriseTestController {

    private final SunriseApiClient sunriseApiClient;

    @GetMapping("/sun")
    @Operation(summary = "일출/일몰 정보 확인용 API")
    public Map<String, String> getSunrise(
            @RequestParam(defaultValue = "인천") String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return sunriseApiClient.getSunriseSunset(location, date);
    }
}