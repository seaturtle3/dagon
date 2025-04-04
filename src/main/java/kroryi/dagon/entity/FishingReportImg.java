package kroryi.dagon.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fishing_report_img")
public class FishingReportImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frimg_id", nullable = false)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "frid")
//    private User frid;

    @Column(name = "frimg_url", nullable = false, length = 512)
    private String frimgUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

}