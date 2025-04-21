package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "faq")
public class FAQ extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id", nullable = false)
    private Long faqId;

    @Column(name = "question", nullable = false)
    private String question;

    @Lob
    @Column(name = "answer", nullable = false)
    private String answer;

    // 표시 순서
    @Column(name = "display_order")
    @ColumnDefault("0")
    private Integer displayOrder;


    // FAQ 활성화 여부
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "modify_at")
    private LocalDateTime modifyAt;

    @PreUpdate
    public void onPreUpdate() {
        this.modifyAt = LocalDateTime.now();
    }

    // 관리자 (프론트에 안보여도 됨)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aid", nullable = false)
    private Admin admin;

}