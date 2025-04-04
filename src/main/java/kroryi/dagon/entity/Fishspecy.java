package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fishspecies")
public class Fishspecy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fiid", nullable = false)
    private Long id;

    @Column(name = "finame")
    private String finame;

    @Column(name = "fiicon_url", length = 500)
    private String fiiconUrl;

}