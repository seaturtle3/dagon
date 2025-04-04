package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid", nullable = false, length = 50)
    private String nid;

    @Column(name = "admin_id", nullable = false, length = 50)
    private String aid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "notice_nickname", nullable = false, length = 50)
    private String noticeNickname;

    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;

    @Column(name = "views")
    private Integer views;

}