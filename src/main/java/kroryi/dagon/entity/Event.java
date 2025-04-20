package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.EventStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

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
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false)
    private EventStatus eventStatus = EventStatus.SCHEDULED;

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


    /**
     * 현재 시간(now)을 기준으로 이벤트 상태(eventStatus)를 자동으로 설정
     * - 시작일과 종료일이 모두 없으면: 상시 진행 중
     * - 현재 시간이 시작일 이전이면: 진행 예정
     * - 현재 시간이 종료일 이후이면: 종료
     * - 그 외(시작일 ~ 종료일 사이): 진행 중
     */
    public void updateEventStatus(LocalDateTime now) {
        if (startAt == null && endAt == null) {
            this.eventStatus = EventStatus.ONGOING; // 상시이벤트:진행중
        } else if (startAt != null && now.isBefore(startAt)) {
            this.eventStatus = EventStatus.SCHEDULED; // 시작전:진행예정
        } else if (endAt != null && now.isAfter(endAt)) {
            this.eventStatus = EventStatus.COMPLETED; // 종료:종료
        } else {
            this.eventStatus = EventStatus.ONGOING; // 진행중:진행중
        }
    }


}
