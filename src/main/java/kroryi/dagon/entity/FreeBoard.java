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
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modify_at", nullable = false)
    private LocalDateTime modifyAt;

    @Column(name = "views")
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User uid;

}