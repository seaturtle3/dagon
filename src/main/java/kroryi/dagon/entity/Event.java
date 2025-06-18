package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.EventStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "event")
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(length = 512)
    private String thumbnailUrl;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    public EventStatus getEventStatus() {
        LocalDate today = LocalDate.now();

        if (startAt != null && today.isBefore(startAt)) {
            return EventStatus.SCHEDULED;
        }

        if (endAt != null && today.isAfter(endAt)) {
            return EventStatus.COMPLETED;
        }

        return EventStatus.ONGOING;
    }

    @Column(name = "views", nullable = false)
    @ColumnDefault("0")
    private int views = 0;

    @Column(name = "is_top", nullable = false)
    @ColumnDefault("false")
    private Boolean isTop = false;

    // 매핑

    // 관리자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "aid", nullable = false)
    private Admin admin;





}
