package kroryi.dagon.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import kroryi.dagon.enums.PaymentsMethod;
import kroryi.dagon.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationDTO {
    private Long reservationId; // 예약 ID

    // 예약자 정보
    private Long userId; // User의 PK

    // 상품 정보
    private Long prodId; // Product의 PK

    // 옵션 정보
    private Long optionId; // ProductOption의 PK

    private Integer numPerson; // 예약 인원
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fishingAt; // 예약 날짜 및 시간
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paidAt; // 결제 시간
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt; // 예약 생성 시간

    private ReservationStatus reservationStatus; // 예약 상태
    private PaymentsMethod paymentsMethod; // 결제 방식

    // 결제 정보
    private String paymentId; // PaymentsEntity의 PK

    // (화면 표시용)
    private String productName;
    private String optionName;
    private String userName;

}
