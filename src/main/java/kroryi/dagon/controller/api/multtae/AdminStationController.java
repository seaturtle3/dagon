package kroryi.dagon.controller.api.multtae;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.service.multtae.StationLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/station")
public class AdminStationController {

    private final StationLinkService stationLinkService;

    @PostMapping("/init-wave")
    @Operation(summary = "TideStation에 가장 가까운 WaveStation 자동 연결")
    public ResponseEntity<String> initWaveLinks() {
        stationLinkService.linkClosestWaveStations();
        return ResponseEntity.ok("자동 연결 완료");
    }
}