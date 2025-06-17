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
    @Query("SELECT p FROM Partner p WHERE p.ceoName LIKE :ceoName")
    Page<Partner> findByCeoNameContaining(@Param("ceoName") String ceoName, Pageable pageable);

    @Query("SELECT p FROM Partner p WHERE p.pAddress LIKE :pAddress")
    Page<Partner> findByPAddressContaining(@Param("pAddress") String pAddress, Pageable pageable);


    @Query("SELECT p FROM Partner p WHERE (:pname IS NULL OR p.pname LIKE :pname)")
    Page<Partner> findByPnameContaining(@Param("pname") String pname, Pageable pageable);




    // 변경 (대소문자 무시, 부분 일치)
    Page<Partner> findByPnameContainingIgnoreCase(String pname, Pageable pageable);
    Page<Partner> findByCeoNameContainingIgnoreCase(String ceoName, Pageable pageable);
    Page<Partner> findBypAddressContainingIgnoreCase(String pAddress, Pageable pageable);





}
