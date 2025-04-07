package kroryi.dagon.DTO;

import kroryi.dagon.entity.User;
import lombok.*;
import org.w3c.dom.Text;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Data
public class UsersDTO {
    private Long uno;
    private String uid;
    private String upw;
    private String uname;
    private String nickname;
    private String email;
    private Text profile_image;
    private int points;
    private int level;
    private String phone1;
    private String phone2;
    private String phone3;
    private String fullPhone;
    private String role;

    // User 객체를 받는 생성자 추가
    public UsersDTO(User user) {
        this.uno = user.getUno();
        this.uid = user.getUid();
        this.upw = user.getUpw();
        this.uname = user.getUname();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.points = user.getPoints();
        this.level = user.getLevel() != null ? user.getLevel().ordinal() : 0;
        this.role = user.getRole() != null ? user.getRole().name() : null;
        this.profile_image = null;  // 필요시 수정
        this.phone1 = "";  // 필요시 수정
        this.phone2 = "";  // 필요시 수정
        this.phone3 = "";  // 필요시 수정
    }

    // 전체 전화번호를 합치는 메서드
    public String getFullPhone() {
        String part1 = (phone1 != null) ? phone1 : "";
        String part2 = (phone2 != null) ? phone2 : "";
        String part3 = (phone3 != null) ? phone3 : "";
        return part1 + "-" + part2 + "-" + part3;
    }
}