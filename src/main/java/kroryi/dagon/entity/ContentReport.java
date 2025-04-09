package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.BoardType;
import kroryi.dagon.enums.ContentReportStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "content_reports")
public class ContentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id",nullable = false)
    private Long reportId;

    // 신고 대상 ID (게시글, 상품 ID)
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // 게시글, 상품 구분
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private BoardType targetType;

    // 신고내용
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    // 관리자 처리 여부
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ContentReportStatus status = ContentReportStatus.PENDING;

    // 처리 일시
    @Column(name = "handled_at")
    private LocalDateTime handledAt;


    // 신고한 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    // 처리한 관리자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by_admin")
    private Admin handledBy;

}
