package kroryi.dagon.DTO;

import kroryi.dagon.entity.User;
import lombok.*;

import java.time.LocalDateTime;

import static kroryi.dagon.enums.LoginType.LOCAL;


@Data
@NoArgsConstructor
@AllArgsConstructor// ✅ 기본 생성자 자동 생성
public class UsersDTO {
    private Long uno;
    private String uid;
    private String upw;
    private String uname;
    private String nickname;
    private String email;
    private String profile_image;
    private int points;
    private int level;
    private String phone1;
    private String phone2;
    private String phone3;
    private String fullPhone;
    private String role;
    private String levelPoint; // levelPoint 추가
    private String loginType;  // 로그인 타입 추가
    private String createdAt;  // 생성일 추가 (BaseTimeEntity에서 자동 생성되는 값)


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
        this.profile_image = user.getProfileImg();  // 필요시 수정
        String[] parts = user.getPhone().split("-");
        this.phone1 = parts.length > 0 ? parts[0] : "";
        this.phone2 = parts.length > 1 ? parts[1] : "";
        this.phone3 = parts.length > 2 ? parts[2] : "";
        this.fullPhone = user.getPhone();
        this.levelPoint = String.valueOf(user.getLevelPoint());
        this.loginType = user.getLoginType() != null ? user.getLoginType().name() : String.valueOf(LOCAL);
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : LocalDateTime.now().toString();
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