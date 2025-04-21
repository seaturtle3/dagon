package kroryi.dagon.repository.multtae;


import kroryi.dagon.entity.multtae.WaveStation;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaveStationRepository extends JpaRepository<WaveStation, String> {
}