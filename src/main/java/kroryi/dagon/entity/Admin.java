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
    @Column(name = "aid", nullable = false, length = 50)
    private Long aid;

    @Column(name = "apw", nullable = false)
    private String apw;

    @Column(name = "aname")
    private String aname;

}