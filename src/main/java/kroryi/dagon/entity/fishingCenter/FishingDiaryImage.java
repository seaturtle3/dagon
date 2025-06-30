package kroryi.dagon.entity.fishingCenter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "fishing_diary_image")
public class FishingDiaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Column(nullable = false, length = 512)
    private String imageUrl;

    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail = false; // true: 대표사진

    @Column(name = "order_index")
    private Integer orderIndex; // 사진 정렬 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fd_id", nullable = false)
    private FishingDiary fishingDiary;

    @Lob
    @Column(name = "thumbnail_data", columnDefinition = "LONGBLOB")
    private byte[] thumbnailData;
}

