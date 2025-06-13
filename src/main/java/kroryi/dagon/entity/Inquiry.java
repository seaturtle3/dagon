package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.entity.User;
import kroryi.dagon.enums.InquiryType;
import kroryi.dagon.enums.ReceiverType;
import kroryi.dagon.enums.WriterType;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인한 유저 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uno", nullable = false)
    private User user;

    // 새 필드 추가
    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    @Builder.Default
    private ReceiverType receiverType = ReceiverType.ADMIN;

    // 파트너에게 보낼 경우만 필요하므로 nullable = true로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_uno")
    private Partner partner;

    // 문의 제목
    @Column(nullable = false)
    private String title;

    // 문의 내용

    @Column(length = 1000) // 예시
    private String content;

    // 작성자 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "writer_type", nullable = false)
    private WriterType writerType;

    // 문의 유형
    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_type", nullable = false)
    private InquiryType inquiryType;

    // 작성일
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 수정일
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 답변 내용
    @Lob
    private String answerContent;

    // 답변 여부
    @Column(nullable = false)
    private boolean isAnswered = false;

    // 답변 시간
    private LocalDateTime answeredAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
