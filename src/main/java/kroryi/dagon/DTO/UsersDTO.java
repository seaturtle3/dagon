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
    private String unickname;
    private String uemail;
    private Text uprofile_image;
    private int upoints;
    private int ulevel;
    private Date ucreated_at;     // 유저 가입 날짜
    private String phone1;
    private String phone2;
    private String phone3;
    private String fullPhone;
    private String urole;

    // User 객체를 받는 생성자 추가
    public UsersDTO(User user) {
        this.uno = user.getId();
        this.uid = user.getUid();
        this.upw = user.getUpw();
        this.uname = user.getUname();
        this.unickname = user.getUnickname();
        this.uemail = user.getUemail();
        this.upoints = user.getUpoints();
        this.ulevel = user.getUlevel() != null ? user.getUlevel().ordinal() : 0;
        this.ucreated_at = java.sql.Timestamp.valueOf(user.getUcreatedAt());
        this.urole = user.getUrole() != null ? user.getUrole().name() : null;
        this.uprofile_image = null;  // 필요시 수정
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

    public String getDisplayName() {
        return (nickname != null && !nickname.isBlank()) ? nickname : uname;
    }
}