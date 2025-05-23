package kroryi.dagon.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import kroryi.dagon.enums.PaymentsMethod;
import kroryi.dagon.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long reservationId; // 예약 ID

    @JsonProperty("prod_name")
    private String productName; // 상품명

    private String optionName; // 옵션명

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fishingAt; // 예약 날짜 및 시간

    @Min(1)
    private Integer numPerson; // 예약 인원 (최소 1명)

    private ReservationStatus reservationStatus; // 예약 상태 (예: PENDING, CONFIRMED)

    private PaymentsMethod paymentsMethod; // 결제 방식 (예: 카카오페이, 신용카드, 무통장입금)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paidAt; // 결제 시간

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt; // 예약 생성 시간
}
