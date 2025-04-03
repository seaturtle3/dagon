package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fishing_diary_img")
public class FishingDiaryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fdimg_id", nullable = false)
    private Long fdimgId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fdid")
    private FishingDiary fishingDiary;

    @Column(name = "fdimg_url", nullable = false, length = 512)
    private String fdimgUrl;

    @Column(name = "uploaded_at", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime uploadedAt;


}
