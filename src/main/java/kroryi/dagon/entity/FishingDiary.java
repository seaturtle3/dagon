package kroryi.dagon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fishingDiary")
public class FishingDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fdid", nullable = false)
    private Long fdid;

    // 테이블 연결 필요 상품아이디(상품), 작성자아이디(유저), 낚시일자(예약)
//    @ManyToOne
//    @JoinColumn(name = "prod_id", nullable = false)
//    private Product product;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    private User uid;

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;

    @Column(name = "fdtitle", nullable = false, length = 50)
    private String fdtitle;

    @Column(name = "fdcontent", nullable = false, columnDefinition = "TEXT")
    private String fdcontent;

    @Column(name = "create_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime create_at;

    @Column(name = "modify_at", columnDefinition = "DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifyAt;

    @Column(name = "views", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer views;
}
