package kroryi.dagon.DTO;

import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link kroryi.dagon.entity.Reservation}
 */
@Value
public class ReservationDTO implements Serializable {
    Long id;
    Integer prodId;
    Long oid;
    Integer uid;
    Instant reservationAt;
    LocalDate fishingAt;
    Integer numPerson;
}