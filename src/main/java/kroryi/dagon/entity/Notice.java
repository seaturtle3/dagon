package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid", nullable = false, length = 50)
    private Long nid;

    @Column(name = "aid", nullable = false, length = 50)
    private Long aid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "notice_title", nullable = false)
    private String noticeTitle;

    @Column(name = "views")
    private Integer views;

}