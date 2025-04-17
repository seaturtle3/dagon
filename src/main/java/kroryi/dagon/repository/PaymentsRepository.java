package kroryi.dagon.repository;

import kroryi.dagon.entity.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<PaymentsEntity, Long> {
    Optional<PaymentsEntity> findByImpUid(String impUid);
}
