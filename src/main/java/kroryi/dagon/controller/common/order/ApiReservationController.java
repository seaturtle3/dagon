package kroryi.dagon.controller.common.order;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.component.CustomUserDetails;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.service.order.SeaFreshwaterFishingService;
import kroryi.dagon.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reservation", description = "예약 생성/조회/취소 API")
@RequestMapping("/api/reservation")
@Log4j2
public class ApiReservationController {

    private final SeaFreshwaterFishingService seaFreshwaterFishingService;
    private final JwtUtil jwtUtil;
    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;

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

            if (!jwtUtil.isValidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            Claims claims = jwtUtil.parseToken(token);
            String role = claims.get("role", String.class);

            System.out.println("role: " + role);
            System.out.println("uno from token: " + claims.get("uno"));
            System.out.println("reservationId: " + reservationId);
            boolean canceled;

            if ("ADMIN".equals(role)) {
                canceled = seaFreshwaterFishingService.cancelReservationByAdmin(reservationId);
            } else if ("USER".equals(role) || "PARTNER".equals(role)) {
                Long uno = Long.parseLong(claims.get("uno").toString());
                canceled = seaFreshwaterFishingService.cancelReservationByUser(reservationId, uno);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            if (canceled) {
                return ResponseEntity.ok("예약이 성공적으로 취소되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("해당 예약을 취소할 수 없습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예약 취소 중 오류 발생");
        }
    }



    @GetMapping
    public ResponseEntity<Page<ReservationDTO>> getReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String searchType,
            @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

        Page<ReservationDTO> result;

        if (keyword == null || keyword.isBlank()) {
            result = seaFreshwaterFishingService.getAllReservations(pageable);
        } else {
            result = seaFreshwaterFishingService.searchReservations(searchType, keyword, pageable);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/partner")
    public ResponseEntity<List<ReservationDTO>> getReservationsForPartner(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long partnerUno = userDetails.getUno();

        List<Reservation> reservations = seaFreshwaterFishingRepository.findByProduct_Partner_Uno(partnerUno);

        List<ReservationDTO> dtoList = reservations.stream().map(r -> ReservationDTO.builder()
                .reservationId(r.getReservationId())
                .productName(r.getProduct().getProdName())
                .optionName(r.getProductOption() != null ? r.getProductOption().getOptName() : "-")
                .userName(r.getUser().getUname())
                .fishingAt(r.getFishingAt())
                .numPerson(r.getNumPerson())
                .reservationStatus(r.getReservationStatus())
                .paymentsMethod(r.getPaymentsMethod())
                .paidAt(r.getPaidAt())
                .createdAt(r.getCreatedAt())
                .build()
        ).toList();

        return ResponseEntity.ok(dtoList);
    }


}
