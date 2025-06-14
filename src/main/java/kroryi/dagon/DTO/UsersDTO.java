package kroryi.dagon.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import kroryi.dagon.entity.Partner;
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
    private String phone;
    private String phone1;
    private String phone2;
    private String phone3;
    private String fullPhone;


    private String role;
    private String levelPoint; // levelPoint 추가
    private String loginType;  // 로그인 타입 추가
    private String createdAt;
    @JsonProperty("isActive")
    private boolean isActive;// 생성일 추가 (BaseTimeEntity에서 자동 생성되는 값)



    // 추가: 파트너 정보 필드들 (필요한 것만)
    private String pname;      // 상호명
    private String ceoName;    // 대표자명
    private String pAddress;   // 주소
    private String pInfo;      // 소개
    private String license;    // 사업자번호
    private String licenseImg; // 사업자 등록증 이미지 경로


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
        this.phone = user.getPhone();
        this.phone1 = parts.length > 0 ? parts[0] : "";
        this.phone2 = parts.length > 1 ? parts[1] : "";
        this.phone3 = parts.length > 2 ? parts[2] : "";
        this.fullPhone = user.getPhone();
        this.levelPoint = String.valueOf(user.getLevelPoint());
        this.loginType = user.getLoginType() != null ? user.getLoginType().name() : String.valueOf(LOCAL);
        this.createdAt = user.getCreatedAt() != null ? user.getCreatedAt().toString() : LocalDateTime.now().toString();
        this.isActive = user.isActive();
    }

    public void setFullPhoneFromParts() {
        this.fullPhone = String.format("%s-%s-%s",
                phone1 != null ? phone1 : "",
                phone2 != null ? phone2 : "",
                phone3 != null ? phone3 : "");
    }

    public void setPhone() {
        this.fullPhone = String.format("%s-%s-%s",
                phone1 != null ? phone1 : "",
                phone2 != null ? phone2 : "",
                phone3 != null ? phone3 : "");
    }

    public String getFullPhone() {
        if (fullPhone == null || fullPhone.isBlank()) {
            setFullPhoneFromParts();
        }
        return fullPhone;
    }

}


