package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "facility")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faid", nullable = false)
    private Long faid;

    @Column(name = "faname")
    private String faname;

    @Column(name = "fa_icon_url", length = 500)
    private String faIconUrl;

}