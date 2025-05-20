package kroryi.dagon.repository;

import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeaFreshwaterFishingRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAll();
    List<Reservation>  findByUser_Uno(Long uno);
    boolean existsByProductOption_OptId(Long optId);


    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.fishingAt >= :now")
    long countFutureReservations(@Param("now") LocalDateTime now);

    // 오늘 날짜 예약 수 (00:00 ~ 23:59:59)
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.fishingAt BETWEEN :startOfDay AND :endOfDay")
    long countTodayReservations(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);


    // 총 예약 수
    long count();

    // 일별 예약 수: 최근 7일 기준, 날짜별 예약 수 집계
    @Query("SELECT DATE(r.createdAt) AS date, COUNT(r) FROM Reservation r " +
            "WHERE r.createdAt >= :startDate GROUP BY DATE(r.createdAt) ORDER BY date ASC")
    List<Object[]> countDailyReservations(@Param("startDate") LocalDateTime startDate);

    // TOP3 예약 많은 파트너 조회
    @Query("SELECT r.product.partner.uno, r.product.partner.pname, COUNT(r) AS cnt " +
            "FROM Reservation r " +
            "GROUP BY r.product.partner.uno, r.product.partner.pname " +
            "ORDER BY cnt DESC")
    List<Object[]> findTop3PartnersByReservationCount(org.springframework.data.domain.Pageable pageable);
}
