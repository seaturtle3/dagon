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
@Table(name = "notice_img")
public class NoticeImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_img_id", nullable = false)
    private Long nImgId;

    @Column(name = "n_img_url", nullable = false, length = 512)
    private String nImgUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nid", nullable = false)
    private kroryi.dagon.entity.Notice nid;
}
