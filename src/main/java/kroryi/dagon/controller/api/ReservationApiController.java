package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;

    @Operation(summary="예약 전 선택한 옵션 조회", description="예약 전 선택한 옵션 조회")
    @GetMapping("/all")
    public String getFindAll(){
        return reservationService.getFindAll();
    }

}
