package kroryi.dagon.controller.admin.station;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.multtae.StationLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Station", description = "관측소 연결 초기화 API (관리자)")
@RequestMapping("/api/admin/station")
public class ApiAdminStationController {

    private final StationLinkService stationLinkService;

    @PostMapping("/init-wave")
    @Operation(summary = "TideStation에 가장 가까운 WaveStation 자동 연결")
    public ResponseEntity<String> initWaveLinks() {
        stationLinkService.linkClosestWaveStations();
        return ResponseEntity.ok("자동 연결 완료");
    }
}