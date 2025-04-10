package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "prod_fishing_gear")
public class ProductFishingGear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fg_id", nullable = false)
    private Long fgId;

    @Column(name = "fg_name", nullable = false)
    private String fgName;

    @Column(name = "fg_icon_url", length = 500)
    private String fgIconUrl;

    @OneToMany(mappedBy = "fg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdFishingGearMapping> productMappings = new ArrayList<>();
}