package kroryi.dagon.repository;

import kroryi.dagon.entity.ProductFishSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FishSpeciesRepository extends JpaRepository<ProductFishSpecies, Long> {
    @Query("SELECT f.fsName FROM ProductFishSpecies f")
    List<String> findFsName();
}
