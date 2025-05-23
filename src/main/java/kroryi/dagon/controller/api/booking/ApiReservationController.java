package kroryi.dagon.controller.api.booking;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.service.ReservationService;
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
@Tag(name = "Booking-Reservation", description = "예약 생성/조회/수정/삭제 API")
@RequestMapping("/api/reservation")
@Log4j2
public class ApiReservationController {

    private final SeaFreshwaterFishingService seaFreshwaterFishingService;
    private final JwtUtil jwtUtil;

    // 모든 예약 옵션 조회
    @Operation(summary = "예약 전 선택한 옵션 조회", description = "예약 전 선택한 옵션 조회")
    @GetMapping("/all")
    public String getFindAll() {
        return seaFreshwaterFishingService.getFindAll();
    }

    // 특정 사용자의 예약 목록 조회
    @GetMapping("/get")
    public ResponseEntity<?> getMyReservations(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());
            List<ReservationDTO> reservations = seaFreshwaterFishingService.getReservationsByUserId(uno);

            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    // 예약 생성 (Create)
    @PostMapping("/create")
    public ResponseEntity<?> createReservation(
            @RequestBody ReservationDTO reservationDTO,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());

            reservationDTO.setCreatedAt(java.time.LocalDateTime.now());
            boolean created = ReservationService.createReservation(uno, reservationDTO);

            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED).body("예약이 성공적으로 생성되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예약 생성 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 생성 중 오류 발생");
        }
    }

    // 예약 수정 (Update)
    @PutMapping("/update/{reservationId}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationDTO reservationDTO,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());

            boolean updated = ReservationService.updateReservation(reservationId, uno, reservationDTO);

            if (updated) {
                return ResponseEntity.ok("예약이 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("예약 수정 권한이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 수정 중 오류 발생");
        }
    }

    // 예약 삭제 (Delete)
    @DeleteMapping("/delete/{reservationId}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable Long reservationId,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            Long uno = Long.parseLong(claims.get("uno").toString());

            boolean deleted = ReservationService.deleteReservation(reservationId, uno);

            if (deleted) {
                return ResponseEntity.ok("예약이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("예약 삭제 권한이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 삭제 중 오류 발생");
        }
    }
}
