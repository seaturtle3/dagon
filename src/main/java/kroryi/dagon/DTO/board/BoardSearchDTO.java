package kroryi.dagon.DTO.board;

import lombok.Data;

@Data
public class BoardSearchDTO {
    private String keyword;
    private String type; // 예: "title", "content", "title+content"
    private Boolean isActive; // (FAQ/이벤트에서만 유용)
}