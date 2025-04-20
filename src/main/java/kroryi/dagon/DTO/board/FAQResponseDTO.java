package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.FAQ;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FAQResponseDTO {
    private Long faqId;
    private String question;
    private String answer;
    private Integer displayOrder;
    private Boolean isActive;
    private String adminName;
    private LocalDateTime createdAt;
    private LocalDateTime modifyAt;

    public static FAQResponseDTO from(FAQ faq) {
        return FAQResponseDTO.builder()
                .faqId(faq.getFaqId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .displayOrder(faq.getDisplayOrder())
                .isActive(faq.getIsActive())
                .adminName(faq.getAdmin().getAname())
                .createdAt(faq.getCreatedAt())
                .modifyAt(faq.getModifyAt())
                .build();
    }
}
