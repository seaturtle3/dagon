package kroryi.dagon.repository;

import kroryi.dagon.entity.PartnerApplication;
import kroryi.dagon.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartnerApplicationRepository extends JpaRepository<PartnerApplication, Long> {



    @Query("SELECT p FROM PartnerApplication p JOIN FETCH p.user")
    List<PartnerApplication> findAllWithUser();  // 페이징용 메서드 아래 따로 작성

    @Query(value = "SELECT p FROM PartnerApplication p JOIN FETCH p.user",
            countQuery = "SELECT COUNT(p) FROM PartnerApplication p")
    Page<PartnerApplication> findAllWithUser(Pageable pageable);


    @Query("""
        SELECT pa FROM PartnerApplication pa
        JOIN FETCH pa.user u
        WHERE (:pname IS NULL OR pa.pname LIKE %:pname%)
        AND (:uname IS NULL OR u.uname LIKE %:uname%)
        AND (:status IS NULL OR pa.pStatus = :status)
    """)
    Page<PartnerApplication> searchWithConditions(
            @Param("pname") String pname,
            @Param("uname") String uname,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );

    @Query("SELECT p FROM PartnerApplication p JOIN FETCH p.user WHERE p.pid = :id")
    Optional<PartnerApplication> findByIdWithUser(@Param("id") Long id);

    long countBypStatus(ApplicationStatus status);
}
