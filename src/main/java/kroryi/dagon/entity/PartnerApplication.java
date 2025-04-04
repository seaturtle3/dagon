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
    private String pname;

    @Column(name = "p_address", nullable = false)
    private String paddress;

    @Column(name = "pceo", length = 50)
    private String pceo;

    @Lob
    @Column(name = "pinfo")
    private String pinfo;

    @Column(name = "plicense", length = 30)
    private String plicense;

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