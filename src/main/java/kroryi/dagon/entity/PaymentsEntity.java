package kroryi.dagon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", indexes = {
    @Index(name = "idx_payments_imp_uid", columnList = "imp_uid"),
    @Index(name = "idx_payments_merchant_uid", columnList = "merchant_uid"),
    @Index(name = "idx_payments_status", columnList = "status"),
    @Index(name = "idx_payments_paid_at", columnList = "paid_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reservation")
public class PaymentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "imp_uid", nullable = false, length = 100, unique = true)
    private String impUid; // 아임포트 결제 고유 ID

    @NotNull
    @Size(max = 100)
    @Column(name = "merchant_uid", nullable = false, length = 100, unique = true)
    private String merchantUid; // 가맹점 주문번호

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // 결제 금액

    @NotNull
    @Size(max = 50)
    @Column(name = "buyer_name", nullable = false, length = 50)
    private String buyerName; // 구매자명

    @NotNull
    @Size(max = 20)
    @Column(name = "status", nullable = false, length = 20)
    private String status; // 결제 상태 (paid, cancelled, failed 등)

    @NotNull
    @Size(max = 20)
    @Column(name = "pay_method", nullable = false, length = 20)
    private String payMethod; // 결제 수단 (card, transfer, vbank 등)

    @Column(name = "paid_at")
    private LocalDateTime paidAt; // 결제 완료 시간

    @OneToOne(mappedBy = "payment", fetch = FetchType.LAZY)
    private Reservation reservation;

    // 결제 상태 확인 메서드
    public boolean isPaid() {
        return "paid".equals(this.status);
    }

    public boolean isCancelled() {
        return "cancelled".equals(this.status);
    }

    public boolean isFailed() {
        return "failed".equals(this.status);
    }
}
