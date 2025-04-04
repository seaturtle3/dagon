package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "partners")
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uno", nullable = false)
    private Long id;



    @Column(name = "pname", nullable = false, length = 50)
    private String pname;

    @Column(name = "paddress", nullable = false)
    private String paddress;

    @Column(name = "pceo", nullable = false, length = 50)
    private String pceo;

    @Lob
    @Column(name = "pinfo")
    private String pinfo;

    @Column(name = "plicense", nullable = false, length = 30)
    private String plicense;

    @Column(name = "plicense_img", nullable = false)
    private String plicenseImg;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "pcreated_at", nullable = false)
    private Instant pcreatedAt;


    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uno", nullable = false)
    private kroryi.dagon.entity.User users;
}