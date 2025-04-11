package kroryi.dagon.DTO;

import lombok.Data;

@Data
public class TideItemDTO {
    private String h1_code; // 고조(만조), 저조(간조)
    private String tph_time;
    private String tph_level; // 조위 필요없을 수도

}
