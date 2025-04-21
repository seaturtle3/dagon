package kroryi.dagon.repository;

import kroryi.dagon.entity.FishingDiaryComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishingDiaryCommentRepository extends JpaRepository<FishingDiaryComment, Long> {

    // ✅ 정확한 필드명에 맞춰 수정
    List<FishingDiaryComment> findByFishingDiary_FdId(Long fdId);
}
