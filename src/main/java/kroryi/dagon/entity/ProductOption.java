package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "prod_option")
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opt_id", nullable = false)
    private Long optId;


    @Column(name = "opt_name", nullable = false)
    private String optName;

    @Column(name = "opt_description", nullable = false)
    private String optDescription;

    @Column(name = "price", precision = 7)
    private BigDecimal price;

    @Column(name = "opt_time", nullable = false)
    private String optTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;


}