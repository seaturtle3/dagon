package kroryi.dagon.DTO;


import lombok.Data;


@Data
public class ReservationCountDTO {

    private long futureReservationCount;
    private long todayReservationCount;

    public ReservationCountDTO(long futureReservationCount, long todayReservationCount) {
        this.futureReservationCount = futureReservationCount;
        this.todayReservationCount = todayReservationCount;
    }

}
