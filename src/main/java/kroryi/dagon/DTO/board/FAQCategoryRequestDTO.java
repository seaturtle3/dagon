package kroryi.dagon.DTO.board;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
public class FAQCategoryRequestDTO {

    private String name;
    private Integer displayOrder;
}
