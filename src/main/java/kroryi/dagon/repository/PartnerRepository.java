package kroryi.dagon.repository;

import kroryi.dagon.entity.PartnerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerApplication, Long> {



}
