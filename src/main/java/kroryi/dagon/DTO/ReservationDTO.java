package kroryi.dagon.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import kroryi.dagon.enums.PaymentsMethod;
import kroryi.dagon.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ReservationDTO {
    private Long reservationId;
    @JsonProperty("prod_name")
    private String productName;
    private String optionName;
    @JsonFormat
    private LocalDateTime fishingAt;
    private Integer numPerson;
    private ReservationStatus reservationStatus;
    private PaymentsMethod paymentsMethod;
    @JsonFormat
    private LocalDateTime paidAt;
    @JsonFormat
    private LocalDateTime createdAt;
}
