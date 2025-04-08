package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "freeboard_comments")
public class FreeboardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid", nullable = false)
    private Long commentid;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "u_nickname", nullable = false, length = 50)
    private String uNickname;

    @Column(name = "comment_content")
    private String commentContent;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fbid", nullable = false)
    private FishingReport fbid;

}