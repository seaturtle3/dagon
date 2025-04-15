package kroryi.dagon.repository;

import kroryi.dagon.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnersRepository extends JpaRepository<Partner, Long> {
}
