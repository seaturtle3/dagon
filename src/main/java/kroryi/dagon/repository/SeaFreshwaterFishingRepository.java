package kroryi.dagon.repository;

import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeaFreshwaterFishingRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAll();
    boolean existsByProductOption_OptId(Long optId);
}
