package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fishing_gear")
public class FishingGear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gid", nullable = false)
    private Long id;

    @Column(name = "gicon_url", length = 500)
    private String giconUrl;

    @Column(name = "gname", nullable = false)
    private String gname;

}