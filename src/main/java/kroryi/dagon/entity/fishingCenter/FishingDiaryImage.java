package kroryi.dagon.entity.fishingCenter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import kroryi.dagon.entity.fishingCenter.FishingDiary;

@Entity
@Table(name = "fishing_diary_image")
@Getter
@Setter
public class FishingDiaryImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;


    @Column(name = "order_index")
    private Integer orderIndex; // 사진 정렬 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fd_id", nullable = false)
    private FishingDiary fishingDiary;

}

