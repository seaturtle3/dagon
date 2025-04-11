package kroryi.dagon.repository;

import kroryi.dagon.entity.ProductFishSpecies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishSpeciesRepository extends JpaRepository<ProductFishSpecies, Long> {
}
