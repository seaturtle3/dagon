package kroryi.dagon.DTO.board;

import jakarta.validation.constraints.NotBlank;
import kroryi.dagon.entity.FAQ;
import lombok.Data;

@Data
public class FAQRequestDTO {

    private Long faqId;
    @NotBlank(message = "질문을 입력")
    private String question;

    @NotBlank(message = "답변을 입력")
    private String answer;

    private Integer displayOrder;

    private Boolean isActive;

    public static FAQRequestDTO from(FAQ faq) {
        FAQRequestDTO dto = new FAQRequestDTO();
        dto.setFaqId(faq.getFaqId());
        dto.setQuestion(faq.getQuestion());
        dto.setAnswer(faq.getAnswer());
        dto.setDisplayOrder(faq.getDisplayOrder());
        dto.setIsActive(faq.getIsActive());
        return dto;
    }
}
