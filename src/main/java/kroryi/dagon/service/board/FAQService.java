package kroryi.dagon.service.board;

import kroryi.dagon.DTO.board.FAQRequestDTO;
import kroryi.dagon.entity.Admin;
import kroryi.dagon.entity.FAQ;
import kroryi.dagon.repository.AdminRepository;
import kroryi.dagon.repository.board.FAQRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;
    private final AdminRepository adminRepository;

    // 전체 FAQ 조회 (관리자용)
    public List<FAQ> findAll() {
        return faqRepository.findAllByOrderByDisplayOrderAsc();
    }

    // 활성화된 FAQ만 조회 (사용자용)
    public List<FAQ> findActiveOnly() {
        return faqRepository.findByIsActiveTrueOrderByDisplayOrderAsc();
    }

    // 단건 조회
    public FAQ findById(Long id) {
        return faqRepository.findById(id).orElse(null);
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
