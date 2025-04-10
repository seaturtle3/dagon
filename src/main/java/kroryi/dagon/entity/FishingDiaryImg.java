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
@Table(name = "fishing_diary_img")
public class FishingDiaryImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fd_img_id", nullable = false)
    private Long fdImgId;

    @Column(name = "fd_img_url", nullable = false, length = 512)
    private String fdImgUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fdid")
    private FishingDiary fdid;

}