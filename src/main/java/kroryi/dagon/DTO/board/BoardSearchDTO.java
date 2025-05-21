package kroryi.dagon.DTO.board;

import lombok.Data;

@Data
public class BoardSearchDTO {
    private String keyword;
    private String type;         // 예: "title", "content", "title+content" (게시판용)
    private Boolean isActive;    // true or false (FAQ/이벤트에서 사용)
    private Long categoryId;
    private String status;       // 사용처에 따라 다름
    private String faqType;      // "question", "answer", "question+answer"
}