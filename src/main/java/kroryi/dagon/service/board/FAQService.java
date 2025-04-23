package kroryi.dagon.service.board;

import kroryi.dagon.DTO.board.BoardSearchDTO;
import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.repository.AdminRepository;
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


    // 검색
    public Page<FAQ> searchPaged(BoardSearchDTO dto, Pageable pageable) {
        if (dto.getKeyword() == null || dto.getKeyword().isBlank()) {
            return findAllPaged(pageable);
        }
        return faqRepository.searchByKeyword(dto.getKeyword(), dto.getType(), pageable);
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

        FAQ faq = new FAQ();
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
