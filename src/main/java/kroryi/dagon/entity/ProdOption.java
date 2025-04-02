package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prod_option", schema = "dagon")
public class ProdOption {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "oid", nullable = false)
    private Long oid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id", nullable = false)
    private Product prodId;

    @Column(name = "oname", nullable = false)
    private String oname;

    @Column(name = "odescription", nullable = false)
    private String odescription;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;  // 상품 가격 (DECIMAL(10, 2))

    @Column(name = "itime", nullable = false)
    private double itime;

}
