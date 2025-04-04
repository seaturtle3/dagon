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
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "upw", nullable = false)
    private String upw;

    @Column(name = "uname")
    private String uname;

    @Column(name = "unickname")
    private String unickname;

    @Column(name = "uemail", nullable = false)
    private String uemail;

    @Column(name = "uprofile_img")
    private String uprofileImg;

    @Column(name = "upoints", nullable = false)
    private Integer upoints;

    @Enumerated(EnumType.STRING) // Enum을 문자열로 저장
    private Level ulevel;

    @Enumerated(EnumType.STRING)
    private Role urole;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ucreated_at", nullable = false)
    private LocalDateTime ucreatedAt;

    @Column(name = "uphone", nullable = false)
    private String uphone;


    @Column(name = "ucreate_at")
    private LocalDateTime ucreateAt;

    public enum Level {
        BRONZE, SILVER, GOLD, PLATINUM
    }

    public enum Role {
        ADMIN, PARTNER, NORMAL_USER
    }

}