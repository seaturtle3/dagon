package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id", nullable = false)
    private Long id;

    @Column(name = "max_person")
    private Integer maxPerson;

    @Column(name = "min_person")
    private Integer minPerson;

    @Column(name = "weight", precision = 38, scale = 2)
    private BigDecimal weight;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "pname")
    private String pname;

    @Column(name = "prod_address")
    private String prodAddress;

    @Column(name = "prod_name")
    private String prodName;

    @Column(name = "uphone")
    private String uphone;

    @Lob
    @Column(name = "prod_description")
    private String prodDescription;

    @Lob
    @Column(name = "prod_event")
    private String prodEvent;

    @Lob
    @Column(name = "prod_notice")
    private String prodNotice;

}