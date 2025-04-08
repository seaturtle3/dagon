package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prod_fish_species")
public class ProductFishSpecies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fsid", nullable = false)
    private Long fsid;

    @Column(name = "fsname")
    private String fsname;

    @Column(name = "fs_icon_url", length = 500)
    private String fsIconUrl;

}
