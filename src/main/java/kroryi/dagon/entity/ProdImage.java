package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prod_img", schema = "dagon")
public class ProdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodImgId;  // 상품이미지아이디 (Primary Key)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id")  // 외래 키 설정
    private Product product;  // 상품 (Product 테이블과의 연관 관계)

    private String prodImgUrl; // 상품이미지경로

    private int prodImgOrder = 0; // 상품이미지순서
}
