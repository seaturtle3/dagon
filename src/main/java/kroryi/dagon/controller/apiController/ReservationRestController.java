package kroryi.dagon.controller.apiController;

import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationRestController {

    private final ReservationService reservationService;

    @GetMapping
    public ReservationDTO getReservation(@RequestParam(required = false) String date,
                                         @RequestParam(required = false) Integer people,
                                         @RequestParam(required = false) String region,
                                         @RequestParam(required = false) String fishType) {

        return new ReservationDTO(date, people, region, fishType);

    }

}
