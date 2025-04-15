package kroryi.dagon.service;

import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.repository.ReservationRepository;
import lombok.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void save(ReservationDTO dto) {
        ReservationDTO reservationDTO = ReservationDTO.builder()
                .date(dto.getDate())
                .people(dto.getPeople())
                .region(dto.getRegion())
                .fishType(dto.getFishType())
                .build();

//        reservationRepository.save(reservation);
    }

}
