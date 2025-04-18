package kroryi.dagon.DTO;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
public class KakaoPayDTO {

    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url; // web - 받는 결제 페이지
    private Date created_at;
}