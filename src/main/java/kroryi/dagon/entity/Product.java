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
        서울특별시, 부산광역시, 대구광역시, 인천광역시, 광주광역시, 대전광역시, 울산광역시, 세종특별시, 경기도, 강원도, 충청북도, 충청남도, 전라북도, 전라남도, 경상북도, 경상남도, 제주도;  // 예시로 지역을 나누었습니다. 실제 데이터에 맞게 수정해야 합니다.
    }

    @Enumerated(EnumType.STRING)
    private MainType mainType;  // 메인분류 (ENUM)
    public enum MainType {
        바다낚시, 민물낚시;  // 바다, 민물
    }

    @Enumerated(EnumType.STRING)
    private SubType subType;  // 서브분류 (ENUM)
    public enum SubType {
        // 바다낚시 서브 분류
        방파제(MainType.바다낚시),
        갯바위(MainType.바다낚시),
        하구(MainType.바다낚시),
        선상(MainType.바다낚시),
        갯벌(MainType.바다낚시),
        인공낚시터(MainType.바다낚시),
        해상(MainType.바다낚시),
        해변(MainType.바다낚시),
        암초(MainType.바다낚시),

        // 민물낚시 서브 분류
        강(MainType.민물낚시),
        저수지(MainType.민물낚시),
        댐(MainType.민물낚시),
        연못(MainType.민물낚시),
        소류지(MainType.민물낚시),
        수로(MainType.민물낚시),
        좌대(MainType.민물낚시),
        노지(MainType.민물낚시);

        private final MainType mainType;

        SubType(MainType mainType) {
            this.mainType = mainType;
        }

        public MainType getMainType() {

            return mainType;

        }
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

