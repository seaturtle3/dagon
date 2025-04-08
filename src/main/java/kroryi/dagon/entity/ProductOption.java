package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "prod_option")
public class ProductOption {

    @Id
    @Column(name = "optid", nullable = false)
    private Long optid;

    @Column(name = "prodid", nullable = false)
    private Long prodid;


    @Column(name = "optname", nullable = false)
    private String optname;

    @Column(name = "opt_description", nullable = false)
    private String optDescription;

    @Column(name = "price", precision = 7)
    private BigDecimal price;

    @Column(name = "opt_time", nullable = false)
    private String optTime;

}