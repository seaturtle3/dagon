package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@Table(name = "partner_applications")
public class PartnerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paid", nullable = false)
    private Long uno;

    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "p_address", nullable = false)
    private String pAddress;

    @Column(name = "ceo", length = 50)
    private String ceo;

    @Lob
    @Column(name = "p_info")
    private String pInfo;

    @Column(name = "p_license", length = 30)
    private String pLicense;

    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String paStatus;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "pa_reviewed_at")
    private LocalDateTime paReviewedAt;

    @Lob
    @Column(name = "pa_rejection_reason")
    private String paRejectionReason;

    @PrePersist
    protected void onCreate() {
        if (this.paStatus == null) {
            this.paStatus = "심사중";
        }
    }


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private User user; // ✅ uno → user 변경
}