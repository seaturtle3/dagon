package kroryi.dagon.DTO;

import kroryi.dagon.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {
    private Long reservationId;
    private String userName;
    private String productName;
    private String optionName;
    private Integer numPerson;
    private LocalDateTime fishingAt;
    private LocalDateTime paidAt;
    private String reservationStatus;
    private String paymentMethod;

    public static ReservationResponseDTO from(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getReservationId(),
                reservation.getUser().getUname(), // getName()은 예시
                reservation.getProduct().getProdName(), // getName()은 예시
                reservation.getProductOption().getOptDescription(), // getName()은 예시
                reservation.getNumPerson(),
                reservation.getFishingAt(),
                reservation.getPaidAt(),
                reservation.getReservationStatus().name(),
                reservation.getPaymentsMethod().name()
        );
    }
}