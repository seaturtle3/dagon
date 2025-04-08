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
public class PartnerApplication extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", nullable = false)
    private Long pid;

    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "p_address", nullable = false)
    private String pAddress;

    @Column(name = "ceo_name", length = 50)
    private String ceoName;

    @Lob
    @Column(name = "p_info")
    private String pInfo;

    @Column(name = "license", length = 30)
    private String license;

    @Column(name = "status", nullable = false)
    private String status;

    // 심사처리 시각
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Lob
    @Column(name = "rejection_reason")
    private String rejectionReason;

    public PartnerApplication() {
        this.status = ApplicationStatus.PENDING.name();
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private User user;


    public enum ApplicationStatus {
        PENDING("심사중"),
        APPROVED("승인됨"),
        REJECTED("반려됨");

        private final String label;

        ApplicationStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

}