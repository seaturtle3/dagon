package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.MainType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "prod_fish_species")
public class ProductFishSpecies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fs_id", nullable = false)
    private Long fsId;

    @Column(name = "fs_name")
    private String fsName;

    @Column(name = "fs_icon_url", length = 500)
    private String fsIconUrl;

    @OneToMany(mappedBy = "fs", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdFishSpeciesMapping> mappedProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MainType mainType;

    public ProductFishSpecies(String fsName, MainType mainType, String fsIconUrl) {
        this.fsName = fsName;
        this.mainType = mainType;
        this.fsIconUrl = fsIconUrl;
    }



}
