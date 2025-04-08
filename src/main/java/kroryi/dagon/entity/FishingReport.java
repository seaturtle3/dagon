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
@Table(name = "fishing_report")
public class FishingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frid", nullable = false)
    private Long frid;

    @Lob
    @Column(name = "fr_content", nullable = false)
    private String frContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(name = "fr_title", nullable = false)
    private String frTitle;

    @ColumnDefault("0")
    @Column(name = "views", nullable = false)
    private Integer views;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pid", nullable = false)
    private kroryi.dagon.entity.User pid;

}