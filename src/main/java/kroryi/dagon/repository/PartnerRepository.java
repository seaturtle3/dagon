package kroryi.dagon.repository;

import kroryi.dagon.entity.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Optional<Partner> findByPname(String pname);

    Optional<Partner> findByUserUno(Long uno);

    Optional<Object> findByUno(Long uno);

    @Query("SELECT p FROM Partner p WHERE (:pname IS NULL OR p.pname LIKE %:pname%)")
    Page<Partner> findByPnameContaining(@Param("pname") String pname, Pageable pageable);
}
