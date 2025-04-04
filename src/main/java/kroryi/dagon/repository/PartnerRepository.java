package kroryi.dagon.repository;

import kroryi.dagon.entity.PartnerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerApplication, Long> {


    // 사업자 등록 번호로 조회 (숫자만 허용)
    boolean existsByPlicense(String plicense);

}
