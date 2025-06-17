package kroryi.dagon.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kroryi.dagon.enums.AdminRole;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {


    @Id
    @Column(name = "aid", nullable = false, length = 50)
    private String aid;

    @Column(name = "apw", nullable = false)
    private String apw;

    @Column(name = "aname")
    private String aname;

    @Enumerated(EnumType.STRING) // enum을 문자열로 저장
    @Column(name = "role")
    private AdminRole role = AdminRole.ADMIN;

    @Column(name = "uno")
    private Long uno;

    // 공지사항
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notice> notices = new ArrayList<>();

    // 신고
    @OneToMany(mappedBy = "handledBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContentReport> handledReports = new ArrayList<>();


}