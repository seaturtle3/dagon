package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "free_board")
public class FreeBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fbid", nullable = false)
    private Long fbid;

    @Column(name = "fb_title", nullable = false, length = 50)
    private String fbTitle;

    @Lob
    @Column(name = "fb_content", nullable = false)
    private String fbContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "fb_created_at")
    private Instant fbCreatedAt;

    @Column(name = "fb_modify", nullable = false)
    private Instant fbModify;

    @Column(name = "views")
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User uid;

}