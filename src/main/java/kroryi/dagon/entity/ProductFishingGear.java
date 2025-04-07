package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fishing_gear")
public class ProductFishingGear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fgid", nullable = false)
    private Long fgid;

    @Column(name = "fg_icon_url", length = 500)
    private String fgIconUrl;

    @Column(name = "fgname", nullable = false)
    private String fgname;

}