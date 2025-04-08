package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "partners")
public class Partner extends BaseTimeEntity {

    @Id
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


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId // uno공유키
    @JoinColumn(name = "uno", nullable = false)
    private User user;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
}