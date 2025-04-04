package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "fishing_report")
public class FishingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "frcontent", nullable = false)
    private String frcontent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "fishing_at", nullable = false)
    private Instant fishingAt;

    @Column(name = "modify_at")
    private Instant modifyAt;

    @Column(name = "frtitle", nullable = false)
    private String frtitle;

    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pid", nullable = false)
    private kroryi.dagon.entity.User pid;

}