package kroryi.dagon.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fishing_report_img")
public class FishingReportImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fr_img_id", nullable = false)
    private Long frImgId;


    @Column(name = "fr_img_url", nullable = false, length = 512)
    private String frImgUrl;

    @Column(name = "fr_uploaded_at", nullable = false, updatable = false)
    private LocalDateTime frUploadedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "frid")
    private FishingReport frId;

}