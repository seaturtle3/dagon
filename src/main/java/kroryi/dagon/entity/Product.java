package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.SubType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id", nullable = false)
    private Long prodId;

    @Column(name = "prod_name", nullable = false)
    private String prodName;

    @Enumerated(EnumType.STRING)
    @Column(name = "prod_region", nullable = false)
    private ProdRegion prodRegion;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_type", nullable = false)
    private MainType mainType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false)
    private SubType subType;

    @Column(name = "max_person")
    private Integer maxPerson;

    @Column(name = "min_person")
    private Integer minPerson;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "prod_address")
    private String prodAddress;

    @Lob
    @Column(name = "prod_description")
    private String prodDescription;

    @Lob
    @Column(name = "prod_event")
    private String prodEvent;

    @Lob
    @Column(name = "prod_notice")
    private String prodNotice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uno", nullable = false)
    private Partner partner;


    // 매핑

    // 상품 이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImg> images = new ArrayList<>();

    // 편의시설
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFacilityMapping> facilityMappings = new ArrayList<>();

    // 낚시도구
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFishingGearMapping> fishingGearMappings = new ArrayList<>();

    // 어종
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFishSpeciesMapping> fishSpeciesMappings = new ArrayList<>();

    // 옵션
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> options = new ArrayList<>();

    // 조황정보
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingReport> fishingReports = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FishingDiary> fishingDiaries = new ArrayList<>();
}