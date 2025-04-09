package kroryi.dagon.service;

import kroryi.dagon.entity.Reservation;
import kroryi.dagon.repository.ReservationRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> search(LocalDate date,
                                    Integer people,
                                    String region,
                                    String fishType) {
        List<Reservation> all = reservationRepository.findAll();
        List<Reservation> result = new ArrayList<>();

        for (Reservation r : all) {
            if (date != null && !r.getRe().equals(date)) continue;
            if (people != null && r.getNumPerson() < people) continue;
            if (region != null && !r.getReservationAt().equals(region)) continue;
            if (fishType != null && !r.getFishType().equals(fishType)) continue;

            result.add(r);
        }
        return result;
    }

}
