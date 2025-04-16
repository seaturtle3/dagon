package kroryi.dagon.repository;

import kroryi.dagon.entity.TideStation;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TideStationRepository extends JpaRepository<TideStation, Integer> {
    List<TideStation> findByRegion(ProdRegion region);
}
