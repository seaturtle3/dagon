package kroryi.dagon.controller;

import kroryi.dagon.entity.Reservation;
import kroryi.dagon.service.ReservationService;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@Getter
@Setter
@ToString
@AllArgsConstructor
@Log4j2
public class ReservationController {

    private final ReservationService reservationService;



    @GetMapping("/reservation")
    public String reservation() {
        return "sub_menu/reservation";
    }

    @GetMapping("/reservation/search")
    public String searchReservation(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    @RequestParam(required = false) Integer people,
                                    @RequestParam(required = false) String reserveAt,
                                    @RequestParam(required = false) String fishType,
                                    Model model) {
        List<Reservation> results = reservationService.search(date, people, reserveAt, fishType);
        model.addAttribute("results", results);



        return "sub_menu/reservation";
    }



}
