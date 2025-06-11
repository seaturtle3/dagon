package kroryi.dagon.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardReservationStatsDTO {
    private long totalReservations;
    private long todayReservations;
    private long upcomingReservations;
}
