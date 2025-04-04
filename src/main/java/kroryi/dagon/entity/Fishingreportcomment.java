package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fishingreportcomments")
public class Fishingreportcomment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fr_comment_id", nullable = false)
    private Long frCommentId;



    @Lob
    @Column(name = "fr_comment_content", nullable = false)
    private String frCommentContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "frid", nullable = false)
    private FishingReport frid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private kroryi.dagon.entity.User uid;

}