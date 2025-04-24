package kroryi.dagon.repository;

import kroryi.dagon.entity.FishingDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FishingDiaryRepository extends JpaRepository<FishingDiary, Long> {
}
