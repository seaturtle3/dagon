package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fishing_diary_comment")
public class FishingDiaryComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fdid", nullable = false)
    private FishingDiary fdid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private kroryi.dagon.entity.User uid;

}