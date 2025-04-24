package kroryi.dagon.service.board;


import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.repository.board.FAQCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQCategoryService {

    private final FAQCategoryRepository faqCategoryRepository;

    public List<FAQCategory> findAll() {
        return faqCategoryRepository.findAllByOrderByDisplayOrderAsc();
    }

    public FAQCategory findById(Long id) {
        return faqCategoryRepository.findById(id).orElseThrow();
    }

    public FAQCategory save(FAQCategory category) {
        return faqCategoryRepository.save(category);
    }

    public void delete(Long id) {
        faqCategoryRepository.deleteById(id);
    }
}