package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prod_img")
public class ProductImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_img_id", nullable = false)
    private Long prodImgId;

    @Column(name = "prod_id", nullable = false)
    private Long prodId;

    @Column(name = "prod_img_order", nullable = false)
    private Integer prodImgOrder;

    @Column(name = "prod_img_url")
    private String prodImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;

}