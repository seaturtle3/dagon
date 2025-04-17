package kroryi.dagon.repository;

import kroryi.dagon.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAll();
    boolean existsByProductOption_OptId(Long optId);
//    List<Reservation> findByReservationAt(Instant reservationAt);       // 예약 장소
//    List<Reservation> findByFishType(String fishType);      // 어종 종류
//    List<Reservation> findByDate(LocalDate date); // 예약 날짜
//     List<Reservation> findBypeople(int people) // 예약 인원



}
