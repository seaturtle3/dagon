package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationDTO {
    private String date;
    private Integer people;
    private String region;
    private String fishType;
}
