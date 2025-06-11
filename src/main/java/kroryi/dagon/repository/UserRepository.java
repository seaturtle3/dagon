package kroryi.dagon.repository;

import kroryi.dagon.entity.Inquiry;
import kroryi.dagon.entity.Report;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    boolean existsByUid(String uid);
    boolean existsByemail(String email);
    Optional<User> findByPhoneAndUname(String phone, String uname);
    Optional<User> findByEmail(String email);

    Optional<User> findByUidAndUnameAndEmail(String uid, String uname, String email);
    Page<User> findByUnameContainingOrEmailContaining(String search, String search1, Pageable pageable);

    Optional<User> findByUno(Long uno);

    @Query("SELECT COUNT(u) FROM User u WHERE DATE(u.createdAt) = CURRENT_DATE")
    long countUsersRegisteredToday();

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = false")
    long countInactiveUsers();

    @Query("SELECT COUNT(DISTINCT r.reported.uno) FROM Report r")
    long countReportedUsers();

    // lastLoginAt이 존재한다는 전제하에:
    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginAt >= :weekAgo")
    long countRecentlyLoggedInUsers(@Param("weekAgo") LocalDateTime weekAgo);

    long countByRole(UserRole role);

    @Query("SELECT u.points FROM User u WHERE u.uno = :uno")
    Integer findPointsByUno(@Param("uno") String uno);




}
