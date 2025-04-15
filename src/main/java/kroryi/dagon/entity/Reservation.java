package kroryi.dagon.entity;

import jakarta.persistence.*;
import kroryi.dagon.enums.PaymentMethod;
import kroryi.dagon.enums.ReservationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    // 예약자 정보
    @Column(name = "uid", nullable = false)
    private Long uid;

    // 상품연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_id", nullable = false)
    private Product product;

    // 옵션연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opt_id", nullable = false)
    private ProductOption productOption;

    // 인원수
    @Column(name = "num_person", nullable = false)
    private Integer numPerson;

    // reservationAt은 예약생성시간으로 이름을 createdAt으로 지정하고z BaseTimeEntity 상속

    @Column(name = "fishing_at", nullable = false)
    private LocalDateTime fishingAt;

    // 결제완료 시간
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    // 예약상태
    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus = ReservationStatus.PENDING;

    // 결제수단
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

}