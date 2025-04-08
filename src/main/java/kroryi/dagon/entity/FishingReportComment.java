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
@Table(name = "fishing_report_comments")
public class FishingReportComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fr_comment_id", nullable = false)
    private Long frCommentId;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String commentContent;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    // 조황정보
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fr_id", nullable = false)
    private FishingReport fishingReport;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    private User user;

}