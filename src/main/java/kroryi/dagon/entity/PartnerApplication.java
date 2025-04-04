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
@Table(name = "partner_applications")
public class PartnerApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paid", nullable = false)
    private Long id;

    @Column(name = "pname", nullable = false, length = 50)
    private String pName;

    @Column(name = "paddress", nullable = false)
    private String pAddress;

    @Column(name = "pceo", length = 50)
    private String pCeo;

    @Lob
    @Column(name = "pinfo")
    private String pInfo;

    @Column(name = "plicense", length = 30)
    private String pLicense;

    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String paStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "pa_created_at", nullable = false)
    private LocalDateTime paCreatedAt;

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
    private kroryi.dagon.entity.User uno;

}