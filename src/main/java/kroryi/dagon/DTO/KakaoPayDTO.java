package kroryi.dagon.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoPayDTO {
    private Long id;
    private String userId;
    private String itemName;
    private String tid; // 결제 고유 번호
    private String amount;
    private String orderId;
    private String next_redirect_pc_url; // web - 받는 결제 페이지
    private Date created_at;
}