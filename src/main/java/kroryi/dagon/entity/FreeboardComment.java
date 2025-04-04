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
    private Long id;


    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "ulevel", nullable = false)
    private Integer ulevel;

    @Column(name = "unickname", nullable = false, length = 50)
    private String unickname;

    @Column(name = "coment_contentt")
    private String comentContentt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fbid", nullable = false)
    private FishingReport fbid;

}