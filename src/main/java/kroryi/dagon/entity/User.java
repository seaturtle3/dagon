package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uno", nullable = false)
    private Long uno;

    @Column(name = "uid", unique = true, nullable = false)
    private String uid;

    @Column(name = "upw", nullable = false)
    private String upw;

    @Column(name = "uname")
    private String uname;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_profile_img")
    private String userProfileImg;

    @Column(name = "user_points", nullable = false)
    private Integer userPoints;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private Level level;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "user_created_at", nullable = false)
    private LocalDateTime userCreatedAt;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    public enum Level {
        SILVER("실버"),
        GOLD("골드"),
        PLATINUM("플래티넘"),
        DIAMOND("다이아몬드");

        private final String korean;

        Level(String koreanName) {
            this.korean = koreanName;
        }

        public String getKorean() {
            return korean;
        }
    }

    public enum Role {
        normal_user, admin
    }

}