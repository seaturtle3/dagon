package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

//    @ManyToOne
//    @JoinColumn(name = "prod_id", nullable = false)
//    private Product product;

    @ManyToOne
    @JoinColumn(name = "pid", nullable = false)
    private User user;

    @Column(name = "frtitle", length = 255, nullable = false)
    private String title;

    @Column(name = "frcontent", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;

    @Column(name = "create_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modify_at", columnDefinition = "DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "views", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int views;
}