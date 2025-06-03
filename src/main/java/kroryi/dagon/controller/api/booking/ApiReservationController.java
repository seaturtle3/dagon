package kroryi.dagon.controller.api.booking;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.service.CustomUserDetailsService;
import kroryi.dagon.service.SeaFreshwaterFishingService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Booking-Reservation", description = "예약 생성/조회/수정/삭제 API")
@RequestMapping("/api/reservation")
@Log4j2
public class ApiReservationController {

    private final SeaFreshwaterFishingService reservationService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "예약 생성", description = "예약을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createReservation(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReservationDTO reservationDTO) {
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            reservationDTO.setUno(userDetails.getUno());

            ReservationDTO saved = reservationService.createReservation(reservationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("예약 생성 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 생성 중 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "예약 전체/검색 조회", description = "전체 예약 또는 검색 조건에 따라 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<ReservationDTO> result = (keyword == null || keyword.isBlank())
                ? reservationService.getAllReservations(pageable)
                : reservationService.searchReservations(searchType, keyword, pageable);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "예약 단건 조회", description = "예약 ID로 예약 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        try {
            ReservationDTO dto = reservationService.getReservationById(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("예약을 찾을 수 없습니다.");
            }
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("예약 조회 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 조회 중 오류 발생");
        }
    }

    @Operation(summary = "예약 수정", description = "예약 ID에 해당하는 예약을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            if (userDetails == null || userDetails.getUno() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
            }

            reservationDTO.setUno(userDetails.getUno());
            ReservationDTO updated = reservationService.updateReservation(id, reservationDTO);

            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("예약 수정 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 수정 중 오류 발생");
        }
    }

    @Operation(summary = "예약 삭제", description = "예약을 취소(삭제)합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "");
            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);
            Long uno = Long.parseLong(claims.get("uno").toString());

            boolean canceled = switch (role) {
                case "ADMIN" -> reservationService.cancelReservationByAdmin(id);
                case "USER" -> reservationService.cancelReservationByUser(id, uno);
                default -> false;
            };

            if (canceled) {
                return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 예약을 취소할 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("예약 삭제 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 중 오류 발생");
        }
    }
}
