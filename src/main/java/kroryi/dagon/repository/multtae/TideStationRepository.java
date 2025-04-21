package kroryi.dagon.repository.multtae;

import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TideStationRepository extends JpaRepository<TideStation, String> {
    List<TideStation> findByRegionOrderByStationNameAsc(ProdRegion region);
}
