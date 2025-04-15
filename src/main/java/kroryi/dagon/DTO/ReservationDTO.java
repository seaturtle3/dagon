package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private String date;
    private Integer people;
    private String region;
    private String fishType;

}
