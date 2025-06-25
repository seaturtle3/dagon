package kroryi.dagon.entity.fishingCenter;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fishing_report_image")
@Getter
@Setter
public class FishingReportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;


    @Column(name = "order_index")
    private Integer orderIndex; // 사진 정렬 순서

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fr_id", nullable = false)
    private FishingReport fishingReport;

}

