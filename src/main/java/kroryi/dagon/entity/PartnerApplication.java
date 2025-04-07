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
    private Long paid;

    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "paddress", nullable = false)
    private String paddress;

    @Column(name = "pceo", length = 50)
    private String pceo;

    @Lob
    @Column(name = "pinfo")
    private String pinfo;

    @Column(name = "plicense", length = 30)
    private String plicense;

    @Enumerated(EnumType.STRING)
    @Column(name = "pa_status", nullable = false)
    private ApplicationStatus paStatus;


    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime paCreatedAt;

    @Column(name = "pa_reviewed_at")
    private LocalDateTime paReviewedAt;

    @Lob
    @Column(name = "pa_rejection_reason")
    private String paRejectionReason;

    public enum ApplicationStatus {
        심사중, 심사완료, 반려
    }

    @PrePersist
    protected void onCreate() {
        if (this.paStatus == null) {
            this.paStatus = ApplicationStatus.심사중;
        }
    }


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private User user; // ✅ uno → user 변경
}