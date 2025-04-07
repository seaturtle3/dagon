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

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private Level level;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "phone", nullable = false)
    private String phone;

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
        USER, PARTNER
    }

}