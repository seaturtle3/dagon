package kroryi.dagon.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.w3c.dom.Text;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class UsersDTO {
    private Long user_no;
    private String id;
    private String pw;
    private String name;
    private String nickname;
    private String email;
    private Text profile_image;
    private int points;
    private int level;
    private Date created_at;     // 유저 가입 날짜
    private String phone;       // 휴대폰 번호
    private String role;        // 역할 (일반 유저, 파트너, 관리자);
}