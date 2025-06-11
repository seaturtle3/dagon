package kroryi.dagon.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.enums.PaymentsMethod;
import kroryi.dagon.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long uno;
    private Long reservationId; // 예약 ID
    @JsonProperty("prod_name")
    private String productName; // 상품명
    private String optionName; // 옵션명
    private String userName; // 사용자명

    @JsonFormat
    private LocalDateTime fishingAt; // 예약 날짜 및 시간
    private Integer numPerson; // 예약 인원 (최소 1명)
    private ReservationStatus reservationStatus; // 예약 상태
    private PaymentsMethod paymentsMethod; // 결제 방식
    @JsonFormat
    private LocalDateTime paidAt; // 결제 시간
    @JsonFormat
    private LocalDateTime createdAt; // 예약 생성 시간



}
