package kroryi.dagon.repository;

import kroryi.dagon.entity.TideStation;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TideStationRepository extends JpaRepository<TideStation, Integer> {
    List<TideStation> findByRegion(ProdRegion region);
}
