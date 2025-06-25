package kroryi.dagon.entity.fishingCenter;

import jakarta.persistence.*;
import kroryi.dagon.entity.BaseTimeEntity;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "fishing_diary")
public class FishingDiary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fd_id", nullable = false)
    private Long fdId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
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

    // 조행기 댓글
    @OneToMany(mappedBy = "fishingDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingDiaryComment> comments = new ArrayList<>();

    // 조행기 사진 목록 (썸네일 포함)
    @OneToMany(mappedBy = "fishingDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingDiaryImage> images = new ArrayList<>();
}