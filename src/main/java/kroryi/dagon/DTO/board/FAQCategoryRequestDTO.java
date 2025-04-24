package kroryi.dagon.DTO.board;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
public class FAQCategoryRequestDTO {
    private Long id; // null이면 등록, 아니면 수정
    private String name;
    private Integer displayOrder;
}
