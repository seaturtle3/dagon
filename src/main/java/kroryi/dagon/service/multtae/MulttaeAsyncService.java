package kroryi.dagon.service.multtae;

import kroryi.dagon.DTO.multtae.AirTempDTO;
import kroryi.dagon.DTO.multtae.TideLevelDTO;
import kroryi.dagon.DTO.multtae.WaveDTO;
import kroryi.dagon.DTO.multtae.WindDTO;
import kroryi.dagon.component.MarineWeatherApiClient;
import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.entity.multtae.WaveStation;
import kroryi.dagon.repository.multtae.WaveStationRepository;
import kroryi.dagon.util.multtae.StationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class MulttaeAsyncService {
    public final MarineWeatherApiClient marineWeatherApiClient;
    private final WaveStationRepository waveStationRepository;

    @Async
    public CompletableFuture<List<WaveDTO>> getWaveDataAsync(TideStation tideStation, String date) {
        String waveStationCode;

        if (tideStation.getWaveStation() != null) {
            waveStationCode = tideStation.getWaveStation().getStationCode();
        } else {
            // 지역 내 waveStation만 필터링
            List<WaveStation> sameRegionWaveStations = waveStationRepository.findAll().stream()
                    .filter(ws -> ws.getRegion() == tideStation.getRegion())
                    .toList();

            if (!sameRegionWaveStations.isEmpty()) {
                WaveStation closest = StationUtil.findClosestWaveStation(tideStation, sameRegionWaveStations);
                waveStationCode = closest.getStationCode();
                log.info("✅ 동일 지역에서 자동 연결된 WaveStation: {} (region={})", waveStationCode, tideStation.getRegion());
            } else {
                log.warn("❌ 지역 '{}'에 속한 WaveStation이 없음. 파고 정보 제외됨 - {}", tideStation.getRegion(), tideStation.getStationCode());
                return CompletableFuture.completedFuture(List.of()); // 빈 리스트 반환
            }
        }
        return CompletableFuture.completedFuture(marineWeatherApiClient.getWaveData(waveStationCode, date));
    }

    @Async
    public CompletableFuture<List<WindDTO>> getWindDataAsync(String stationCode, String date) {
        return CompletableFuture.completedFuture(marineWeatherApiClient.getWindData(stationCode, date));
    }

    @Async
    public CompletableFuture<List<AirTempDTO>> getAirDataAsync(String stationCode, String date) {
        return CompletableFuture.completedFuture(marineWeatherApiClient.getAirTempData(stationCode, date));
    }

    @Async
    public CompletableFuture<List<TideLevelDTO>> getTideDataAsync(String stationCode, String date) {
        return CompletableFuture.completedFuture(marineWeatherApiClient.getTideLevelData(stationCode, date));
    }
}
