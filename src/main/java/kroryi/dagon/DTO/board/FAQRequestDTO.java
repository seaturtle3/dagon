package kroryi.dagon.DTO.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FAQRequestDTO {
    @NotBlank(message = "질문을 입력")
    private String question;

    @NotBlank(message = "답변을 입력")
    private String answer;

    private Integer displayOrder;

    private Boolean isActive;
}
