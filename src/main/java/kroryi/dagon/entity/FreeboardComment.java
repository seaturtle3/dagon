package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "freeboard_comments")
public class FreeboardComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "user_nickname", nullable = false, length = 50)
    private String userNickname;

    @Column(name = "coment_contentt")
    private String comentContentt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fbid", nullable = false)
    private FishingReport fbid;

}