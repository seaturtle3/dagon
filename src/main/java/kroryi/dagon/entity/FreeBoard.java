package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "free_board")
public class FreeBoard extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fb_id", nullable = false)
    private Long fbId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(length = 512)
    private String thumbnailUrl;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer views = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "freeBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeBoardComment> comments = new ArrayList<>();

}