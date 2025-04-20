package kroryi.dagon.DTO.board;

import kroryi.dagon.entity.FAQ;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FAQListDTO {
    private Long faqId;
    private String question;
    private Boolean isActive;
    private Integer displayOrder;

    public static FAQListDTO from(FAQ faq) {
        return FAQListDTO.builder()
                .faqId(faq.getFaqId())
                .question(faq.getQuestion())
                .isActive(faq.getIsActive())
                .displayOrder(faq.getDisplayOrder())
                .build();
    }
}
