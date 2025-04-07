package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aid", nullable = false, length = 50)
    private String aid;

    @Column(name = "apw", nullable = false)
    private String apw;

    @Column(name = "aname")
    private String aname;

}