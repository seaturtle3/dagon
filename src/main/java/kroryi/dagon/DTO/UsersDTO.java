package kroryi.dagon.DTO;

import lombok.*;
import org.w3c.dom.Text;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Data
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
    private String phone1;
    private String phone2;
    private String phone3;
    private String fullPhone;
    private String role;
    // 전체 전화번호를 합치는 메서드
    public String getFullPhone() {
        String part1 = (phone1 != null) ? phone1 : "";
        String part2 = (phone2 != null) ? phone2 : "";
        String part3 = (phone3 != null) ? phone3 : "";
        return part1 + "-" + part2 + "-" + part3;
    }      // 휴대폰 번호






}