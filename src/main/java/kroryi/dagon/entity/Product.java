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


    @Enumerated(EnumType.STRING)
    @Column(name = "prod_region", nullable = false)
    private ProdRegion prodRegion;  // 지역 (ENUM)

    public enum ProdRegion {
        seoul("서울"),
        busan("부산"),
        daegu("대구"),
        incheon("인천"),
        gwangju("광주"),
        daejeon("대전"),
        ulsan("울산"),
        sejong("세종"),
        gyeonggi("경기도"),
        gangwon("강원도"),
        chungcheongbuk("충청북도"),
        chungcheongnam("충청남도"),
        jeollabuk("전라북도"),
        jeollanam("전라남도"),
        gyeongsangbuk("경상북도"),
        gyeongsangnam("경상남도"),
        jeju("제주");

        private final String korean;

        ProdRegion(String koreanName) {
            this.korean = koreanName;
        }

        public String getKorean() {
            return korean;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "main_type", nullable = false)
    private MainType mainType;

    public enum MainType {
        sea("바다낚시"),
        freshwater("민물낚시");

        private final String korean;

        MainType(String korean) {
            this.korean = korean;
        }

        public String getKorean() {
            return korean;
        }
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_type", nullable = false)
    private SubType subType;

    public enum SubType {
        // 바다낚시 서브 분류
        break_water("방파제", MainType.sea),
        rocky_shore("갯바위", MainType.sea),
        estuary("하구", MainType.sea),
        boat("선상", MainType.sea),
        mud_flat("갯벌", MainType.sea),
        artificial("인공낚시터", MainType.sea),
        open_sea("해상", MainType.sea),
        beach("해변", MainType.sea),
        reef("암초", MainType.sea),
        other_sea("기타_바다", MainType.sea),

        // 민물낚시 서브 분류
        river("강", MainType.freshwater),
        reservoir("저수지", MainType.freshwater),
        dam("댐", MainType.freshwater),
        pond("연못", MainType.freshwater),
        small_lake("소류지", MainType.freshwater),
        canal("수로", MainType.freshwater),
        floating_platform("좌대", MainType.freshwater),
        open_area("노지", MainType.freshwater),
        other_freshwater("기타_민물", MainType.freshwater);

        private final String korean;
        private final MainType mainType;

        SubType(String korean, MainType mainType) {
            this.korean = korean;
            this.mainType = mainType;
        }

        public String getKorean() {
            return korean;
        }

        public MainType getMainType() {
            return mainType;
        }
    }







    //

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


    public enum Level {
        Bronze, Silver, Gold, Platinum
    }

}