package kroryi.dagon.repository;

import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kroryi.dagon.enums.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeaFreshwaterFishingRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAll();

    List<Reservation> findByUser_Uno(Long uno);

    boolean existsByProductOption_OptId(Long optId);


    List<Reservation> findTop10ByUserOrderByCreatedAtDesc(User user);

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

    @Query(value = "SELECT r FROM Reservation r " +
            "JOIN FETCH r.product p " +
            "JOIN FETCH r.productOption o " +
            "JOIN FETCH r.user u",
            countQuery = "SELECT count(r) FROM Reservation r")
    Page<Reservation> findAllWithDetails(Pageable pageable);


    // 파트너 페이지 예악자 조회용
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user u WHERE u.uno = :uno")
    List<Reservation> findByUserUno(@Param("uno") Long uno);

    // 상품명 검색
    @Query(value = "SELECT r FROM Reservation r JOIN r.product p WHERE LOWER(p.prodName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Reservation> findByProductNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    // 예약자명 검색
    @Query(value = "SELECT r FROM Reservation r JOIN r.user u WHERE LOWER(u.uname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Reservation> findByUserNameContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    List<Reservation> findAllByProduct_Partner_Uno(Long partnerUno);



    long countByFishingAtAfter(LocalDateTime today);

    long countByFishingAtBetween(LocalDateTime today, LocalDateTime tomorrow);

    // ========== 관리자용 메서드들 ==========

    /**
     * 관리자용 키워드 검색 (예약번호, 회원명, 파트너명)
     */
    @Query(value = "SELECT r FROM Reservation r " +
            "JOIN FETCH r.product p " +
            "JOIN FETCH r.productOption o " +
            "JOIN FETCH r.user u " +
            "JOIN FETCH p.partner pt " +
            "WHERE CAST(r.reservationId AS string) LIKE %:keyword% " +
            "OR LOWER(u.uname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(pt.pname) LIKE LOWER(CONCAT('%', :keyword, '%'))",
            countQuery = "SELECT count(r) FROM Reservation r " +
                    "JOIN r.product p " +
                    "JOIN r.productOption o " +
                    "JOIN r.user u " +
                    "JOIN p.partner pt " +
                    "WHERE CAST(r.reservationId AS string) LIKE %:keyword% " +
                    "OR LOWER(u.uname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "OR LOWER(pt.pname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Reservation> findBySearchKeywordForAdmin(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 관리자용 날짜별 예약 조회
     */
    @Query(value = "SELECT r FROM Reservation r " +
            "JOIN FETCH r.product p " +
            "JOIN FETCH r.productOption o " +
            "JOIN FETCH r.user u " +
            "WHERE r.fishingAt BETWEEN :startDate AND :endDate",
            countQuery = "SELECT count(r) FROM Reservation r " +
                    "WHERE r.fishingAt BETWEEN :startDate AND :endDate")
    Page<Reservation> findByFishingAtBetweenForAdmin(@Param("startDate") LocalDateTime startDate, 
                                                     @Param("endDate") LocalDateTime endDate, 
                                                     Pageable pageable);

    /**
     * 관리자용 상태별 예약 조회
     */
    @Query(value = "SELECT r FROM Reservation r " +
            "JOIN FETCH r.product p " +
            "JOIN FETCH r.productOption o " +
            "JOIN FETCH r.user u " +
            "WHERE r.reservationStatus = :status",
            countQuery = "SELECT count(r) FROM Reservation r " +
                    "WHERE r.reservationStatus = :status")
    Page<Reservation> findByReservationStatusForAdmin(@Param("status") ReservationStatus status, Pageable pageable);

    /**
     * 상태별 예약 수 카운트
     */
    long countByReservationStatus(ReservationStatus status);
}
