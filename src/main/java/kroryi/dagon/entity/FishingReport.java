package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fishing_report")
public class FishingReport extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fr_id", nullable = false)
    private Long frId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 512)
    private String thumbnailUrl;

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer views = 0;


    // 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;

    // 조황정보 댓글
    @OneToMany(mappedBy = "fishingReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingReportComment> comments = new ArrayList<>();

}