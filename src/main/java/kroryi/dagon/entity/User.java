package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.UserLevel;
import kroryi.dagon.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

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

    @Column(name = "phone", nullable = false)
    private String phone;

    @ColumnDefault("0")
    @Column(name = "points", nullable = false)
    private Integer points = 0;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private UserLevel level = UserLevel.SILVER;

    @ColumnDefault("0")
    @Column(name = "level_point", nullable = false)
    private Integer levelPoint = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;


    // 매핑

    // 파트너신청
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<PartnerApplication> partnerApplications;

    // 승인 파트너
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Partner partner;

    // 조황정보
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingReport> fishingReports = new ArrayList<>();

    // 조행기
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingDiary> fishingDiaries = new ArrayList<>();

    // 자유게시판
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeBoard> freeBoards = new ArrayList<>();

    // 찜, 좋아요
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAction> userActions = new ArrayList<>();
}