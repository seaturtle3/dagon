package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQRepository extends JpaRepository<FAQ, Long> {
    // 표시 순서 기준 정렬된 전체 리스트 (활성 여부 포함) (관리자페이지용)
    List<FAQ> findAllByOrderByDisplayOrderAsc();

    // 활성화된 FAQ만 가져오기 (사용자페이지용)
    List<FAQ> findByIsActiveTrueOrderByDisplayOrderAsc();
}
