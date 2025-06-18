package kroryi.dagon.entity;


import jakarta.persistence.*;
import kroryi.dagon.enums.TargetType;
import lombok.*;

@Entity
@Table(name = "user_reports")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 신고한 사람 (선택)
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    // 신고 대상 ID (게시글, 댓글, 상품 ID) - 외래키 연결 안함
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // 신고당한 사람 (필수)
    @ManyToOne
    @JoinColumn(name = "reported_id", nullable = false)
    private User reported;

    @Column(nullable = false)
    private String reason; // ⬅️ 신고 내용
}
