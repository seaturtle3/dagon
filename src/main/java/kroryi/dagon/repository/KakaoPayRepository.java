package kroryi.dagon.repository;

import kroryi.dagon.DTO.KakaoPayDTO;
import kroryi.dagon.entity.KakaoPayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoPayRepository extends JpaRepository<KakaoPayEntity, Long> {
    Optional<KakaoPayEntity> findByTid(String tid);
}
