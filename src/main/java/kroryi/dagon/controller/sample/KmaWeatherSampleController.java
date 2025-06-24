package kroryi.dagon.controller.sample;

import kroryi.dagon.component.KmaVilageFcstApiClient;
import kroryi.dagon.component.KmaMidFcstApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sample/weather")
public class KmaWeatherSampleController {

    private final KmaVilageFcstApiClient vilageFcstApiClient;
    private final KmaMidFcstApiClient midFcstApiClient;

    /**
     * 단기예보(동네예보) 샘플 호출
     */
    @GetMapping("/vilagefcst")
    public String getVilageFcst(
            @RequestParam int nx,
            @RequestParam int ny,
            @RequestParam String baseDate,
            @RequestParam String baseTime
    ) {
        return vilageFcstApiClient.getVilageFcst(nx, ny, baseDate, baseTime);
    }

    /**
     * 중기예보(육상) 샘플 호출
     */
    @GetMapping("/midfcst")
    public String getMidFcst(
            @RequestParam String regId,
            @RequestParam String tmFc
    ) {
        return midFcstApiClient.getMidLandFcst(regId, tmFc);
    }
} 