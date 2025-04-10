package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private List<ProductFishSpeciesMapping> mappedProducts = new ArrayList<>();
}
