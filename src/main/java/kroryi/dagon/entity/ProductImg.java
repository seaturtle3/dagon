package kroryi.dagon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prod_img")
public class ProductImg {
    @Id
    @Column(name = "prod_img_id", nullable = false)
    private Long id;

    @Column(name = "prod_img_order", nullable = false)
    private Integer prodImgOrder;

    @Column(name = "prod_img_url")
    private String prodImgUrl;

}