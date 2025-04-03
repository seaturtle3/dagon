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
    public enum ProdRegion {
        NORTH, SOUTH, EAST, WEST, OTHER;  // 예시로 지역을 나누었습니다. 실제 데이터에 맞게 수정해야 합니다.
    }

    @Enumerated(EnumType.STRING)
    private MainType mainType;  // 메인분류 (ENUM)
    public enum MainType {
        SEA, FRESHWATER, OTHER;  // 바다, 민물, 기타
    }

    @Enumerated(EnumType.STRING)
    private SubType subType;  // 서브분류 (ENUM)
    public enum SubType {
        SUBTYPE1, SUBTYPE2, SUBTYPE3;  // 예시로 서브분류를 추가했습니다. 실제 데이터에 맞게 수정해야 합니다.
    }

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

