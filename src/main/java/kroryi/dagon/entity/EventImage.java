package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(name = "event_image")
@EntityListeners(AuditingEntityListener.class)
public class EventImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    private Event event;

    @Lob
    private byte[] imageData;

    @Lob
    private byte[] thumbnailData;

    private String imageUrl;

    private Boolean isThumbnail = false; // 썸네일 여부

    private String imageType; // "editor" or "thumbnail"

    @Column(name = "order_index")
    private Integer orderIndex; // 이미지 순서

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    public void setIsThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }
}