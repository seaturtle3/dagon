package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fishing_diary")
public class FishingDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fdid", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Lob
    @Column(name = "fdcontent", nullable = false)
    private String fdcontent;

    @Column(name = "fdtitle", nullable = false, length = 50)
    private String fdtitle;

    @Column(name = "fishing_at", nullable = false)
    private Instant fishingAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private kroryi.dagon.entity.User uid;

}