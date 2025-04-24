package kroryi.dagon.service.multtae;

import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.entity.multtae.WaveStation;
import kroryi.dagon.repository.multtae.TideStationRepository;
import kroryi.dagon.repository.multtae.WaveStationRepository;
import kroryi.dagon.util.multtae.StationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationLinkService {

    private final TideStationRepository tideStationRepository;
    private final WaveStationRepository waveStationRepository;

    @Transactional
    public void linkClosestWaveStations() {
        List<TideStation> tideStations = tideStationRepository.findAll();
        List<WaveStation> waveStations = waveStationRepository.findAll();

        for (TideStation tide : tideStations) {
            WaveStation closest = StationUtil.findClosestWaveStation(tide, waveStations);
            tide.setWaveStation(closest); // 연관관계 설정
        }

        // JPA 변경 감지로 저장됨, saveAll 안 해도 OK
    }
}
