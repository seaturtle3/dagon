package kroryi.dagon.repository;

import kroryi.dagon.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Long countByProduct_Partner_Uno(Long partnerId);
    Long countByProduct_Partner_UnoAndFishingAtBetween(Long partnerId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT r FROM Reservation r " +
           "JOIN FETCH r.user u " +
           "JOIN FETCH r.product p " +
           "JOIN FETCH r.productOption po " +
           "WHERE r.product.partner.uno = :partnerId " +
           "ORDER BY r.createdAt DESC")
    List<Reservation> findTop3ByProduct_Partner_UnoOrderByCreatedAtDesc(@Param("partnerId") Long partnerId);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.createdAt BETWEEN :start AND :end")
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.fishingAt BETWEEN :start AND :end")
    long countByFishingAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.fishingAt > :date")
    long countByFishingAtAfter(LocalDateTime date);

    List<Reservation> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT r FROM Reservation r WHERE r.createdAt > :date ORDER BY r.createdAt DESC")
    List<Reservation> findByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime date);

    List<Reservation> findTop5ByOrderByCreatedAtDesc();
} 