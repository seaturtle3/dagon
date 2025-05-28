package kroryi.dagon.controller.user.order;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.service.SeaFreshwaterFishingService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "예약 API (사용자)")
@RequestMapping("/api/reservation")
@Log4j2
public class ApiUserReservationController {

    private final SeaFreshwaterFishingService seaFreshwaterFishingService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "예약 전 선택한 옵션 조회", description = "예약 전 선택한 옵션 조회")
    @GetMapping("/all")

    public String getFindAll() {
        return seaFreshwaterFishingService.getFindAll();
    }

    @PostMapping("/post")
    public ResponseEntity<List<ReservationDTO>> postMyReservations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);  // 인증되지 않은 경우 처리
        }

        Long uno = userDetails.getUno();
        List<ReservationDTO> reservations = seaFreshwaterFishingService.getReservationsByUserId(uno);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getMyReservations(@RequestHeader("Authorization") String authorization) {

        try{
            // Bearer <token> 형식에서 토큰만 추출
            String token = authorization.replace("Bearer ", "");

            // JWT 검증 및 데이터 반환
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");

            }
            // 토큰이 유효하면 예약 정보 반환
            // uno 추출
            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());

            log.info("222222222->{}", uno);
            // 예약 정보 조회
            List<ReservationDTO> reservations = seaFreshwaterFishingService.getReservationsByUserId(uno);

            log.info("222222223->{}", reservations.toString());

            return ResponseEntity.ok(reservations);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }


    }

    @DeleteMapping("/cancel/{reservationId}")
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");

            // 토큰 유효성 검사
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            // 사용자 uno 추출
            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());

            // 취소 서비스 호출
            boolean canceled = seaFreshwaterFishingService.cancelReservationByUser(reservationId, uno);

            if (canceled) {
                return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 예약을 취소할 수 없습니다.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 중 오류 발생");
        }
    }


}
