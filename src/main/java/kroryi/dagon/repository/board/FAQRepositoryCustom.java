package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FAQRepositoryCustom {
    Page<FAQ> searchDynamic(String keyword, Long categoryId, Boolean isActive, Pageable pageable);
} 