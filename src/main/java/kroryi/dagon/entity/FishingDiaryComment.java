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
@Table(name = "fishing_diary_comment")
public class FishingDiaryComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fd_comment_id", nullable = false)
    private Long fdCommentID;

    @Lob
    @Column(name = "fd_comment_content", nullable = false)
    private String fdCommentContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "cmodify_at")
    private LocalDateTime modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fdid", nullable = false)
    private FishingDiary fdId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private kroryi.dagon.entity.User uid;

}