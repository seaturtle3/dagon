package kroryi.dagon.repository.board;

import kroryi.dagon.entity.FAQCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FAQCategoryRepository extends JpaRepository<FAQCategory, Long> {
    List<FAQCategory> findAllByOrderByDisplayOrderAsc();
}