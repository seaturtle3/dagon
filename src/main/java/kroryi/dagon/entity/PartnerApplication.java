package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "partner_applications")
public class PartnerApplication extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", nullable = false)
    private Long pid;

    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "ceo_name", length = 50)
    private String ceoName;

    @Column(name = "p_address", nullable = false)
    private String pAddress;

    @Lob
    @Column(name = "p_info")
    private String pInfo;

    @Column(name = "license", length = 30)
    private String license;

    @Enumerated(EnumType.STRING)
    @Column(name = "p_status", nullable = false)
    private ApplicationStatus pStatus = ApplicationStatus.PENDING;

    @Column(name = "p_reviewed_at")
    private LocalDateTime pReviewedAt;

    @Lob
    @Column(name = "p_rejection_reason")
    private String pRejectionReason;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private User user; // ✅ uno → user 변경
}