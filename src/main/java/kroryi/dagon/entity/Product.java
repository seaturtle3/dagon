package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prodId;  // 상품아이디 (Primary Key)

    private String prodName;  // 상품명

    @Enumerated(EnumType.STRING)
    private ProdRegion prodRegion;  // 지역 (ENUM)

    @Enumerated(EnumType.STRING)
    private MainType mainType;  // 메인분류 (ENUM)

    @Enumerated(EnumType.STRING)
    private SubType subType;  // 서브분류 (ENUM)

    private String pname;  // 파트너업체명

    private String uphone;  // 업체 연락처

    private LocalDateTime createdAt;  // 상품등록일

    private String prodAddress;  // 상품주소

    private BigDecimal weight;  // 중량 (단위: 톤)

    private Integer minPerson;  // 최소인원

    private Integer maxPerson;  // 최대인원

    @Lob
    private String prodDescription;  // 상품소개

    @Lob
    private String prodNotice;  // 상품공지

    @Lob
    private String prodEvent;  // 상품이벤트

    // 기본 생성자, getters, setters, toString 자동 생성됨
}

