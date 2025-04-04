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
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Column(name = "admin_id", nullable = false, length = 50)
    private String aid;

    @Column(name = "admin_pw", nullable = false)
    private String apw;

}