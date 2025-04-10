package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "prod_facility")
public class ProductFacility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fa_id", nullable = false)
    private Long faId;

    @Column(name = "fa_name")
    private String faName;

    @Column(name = "fa_icon_url", length = 500)
    private String faIconUrl;

    @OneToMany(mappedBy = "fa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdFacilityMapping> facilityMappings = new ArrayList<>();

}