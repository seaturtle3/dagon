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
@Table(name = "partners")
public class Partner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uno", nullable = false)
    private Long uno;

    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "p_address", nullable = false)
    private String pAddress;

    @Column(name = "ceo_name", nullable = false, length = 50)
    private String ceoName;

    @Lob
    @Column(name = "p_info")
    private String pInfo;

    @Column(name = "license", length = 30)
    private String license;

    @Column(name = "license_img")
    private String licenseImg;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "p_created_at", nullable = false)
    private LocalDateTime pCreatedAt;


    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private User user;
}