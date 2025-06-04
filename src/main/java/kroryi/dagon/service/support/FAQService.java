package kroryi.dagon.service.support;

import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.entity.FAQCategory;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.FAQCategoryRepository;
import kroryi.dagon.repository.board.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;
    private final AdminRepository adminRepository;
    private final FAQCategoryRepository faqCategoryRepository;


    // 전체 FAQ 조회 (관리자용)
    public Page<FAQ> findAllPaged(Pageable pageable) {
        return faqRepository.findAllByOrderByDisplayOrderAsc(pageable);
    }

    // 활성화된 FAQ만 조회 (사용자용)
    public Page<FAQ> findActivePaged(Pageable pageable) {
        return faqRepository.findByIsActiveTrueOrderByDisplayOrderAsc(pageable);
    }


    // 단건 조회
    public FAQ findById(Long id) {
        return faqRepository.findById(id).orElse(null);
    }


    public Page<FAQ> searchFaq(BoardSearchDTO dto, Pageable pageable) {
        String keyword = dto.getKeyword();
        String faqType = dto.getFaqType(); // "question", "answer", "question+answer"
        Boolean isActive = dto.getIsActive();
        Long categoryId = dto.getCategoryId();

        // 검색 키워드가 있을 경우 (question/answer 필터와 함께)
        if (keyword != null && !keyword.isBlank()) {
            if (isActive == null) {
                return faqRepository.searchByKeyword(keyword, faqType, pageable);
            } else if (isActive) {
                return faqRepository.searchByKeywordAndActive(keyword, faqType, pageable);
            } else {
                // 아직 inactive 전용 메서드가 없다면, 직접 만들거나 조건 추가 필요
                return faqRepository.searchByKeywordAndInactive(keyword, faqType, pageable);
            }
        }

        // 키워드가 없고 카테고리만 있는 경우
        if (categoryId != null) {
            return faqRepository.searchByCategoryAndKeyword(categoryId, null, pageable);
        }

        // 아무 조건이 없는 경우 전체 조회
        return faqRepository.findAllByOrderByDisplayOrderAsc(pageable);
    }

    public Page<FAQ> searchActivePaged(BoardSearchDTO dto, Pageable pageable) {
        if (dto.getKeyword() == null || dto.getKeyword().isBlank()) {
            return findActivePaged(pageable);
        }
        return faqRepository.searchByKeywordAndActive(dto.getKeyword(), dto.getType(), pageable);
    }

    // 등록
    @Transactional
    public FAQ createFAQ(FAQRequestDTO dto, String adminId) {
        Admin admin = adminRepository.findById(adminId).orElseThrow();
        FAQCategory category = faqCategoryRepository.findById(dto.getCategoryId()).orElseThrow();

        FAQ faq = new FAQ();
        faq.setCategory(category);
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setDisplayOrder(dto.getDisplayOrder());
        faq.setIsActive(dto.getIsActive() != null && dto.getIsActive());
        faq.setAdmin(admin);

        return faqRepository.save(faq);
    }

    // 수정
    @Transactional
    public FAQ updateFAQ(Long id, FAQRequestDTO dto) {
        FAQ faq = faqRepository.findById(id).orElseThrow();
        FAQCategory category = faqCategoryRepository.findById(dto.getCategoryId()).orElseThrow();

        faq.setCategory(category);
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setDisplayOrder(dto.getDisplayOrder());
        faq.setIsActive(dto.getIsActive() != null && dto.getIsActive());

        return faq;
    }

    // 삭제
    @Transactional
    public void deleteFAQ(Long id) {
        FAQ faq = faqRepository.findById(id).orElseThrow();
        faqRepository.delete(faq);
    }

}
