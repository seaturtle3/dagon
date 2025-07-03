package kroryi.dagon.controller.admin.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.service.order.SeaFreshwaterFishingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reservations")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Reservation", description = "관리자 예약 관리 API")
@Log4j2
public class ApiAdminReservationController {

    private final SeaFreshwaterFishingService reservationService;

    @Operation(summary = "관리자용 예약 목록 조회", description = "검색 조건에 따른 예약 목록을 조회합니다.")
    @GetMapping("/post")
    public ResponseEntity<Map<String, Object>> getAllReservations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            
            // 검색 조건에 따른 예약 조회
            Page<ReservationDTO> reservations;
            
            if (search != null && !search.trim().isEmpty()) {
                // 검색어가 있는 경우
                reservations = reservationService.searchReservationsForAdmin(search, pageable);
            } else if (date != null && !date.trim().isEmpty()) {
                // 날짜 필터가 있는 경우
                LocalDate filterDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
                reservations = reservationService.getReservationsByDateForAdmin(filterDate, pageable);
            } else if (status != null && !status.trim().isEmpty()) {
                // 상태 필터가 있는 경우
                reservations = reservationService.getReservationsByStatusForAdmin(status, pageable);
            } else {
                // 모든 예약 조회
                reservations = reservationService.getAllReservationsForAdmin(pageable);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("content", reservations.getContent());
            response.put("totalElements", reservations.getTotalElements());
            response.put("totalPages", reservations.getTotalPages());
            response.put("currentPage", reservations.getNumber());
            response.put("size", reservations.getSize());

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("관리자 예약 목록 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "예약 목록 조회 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "예약 승인", description = "예약을 승인합니다.")
    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String, Object>> approveReservation(@PathVariable Long id) {
        try {
            boolean success = reservationService.approveReservationByAdmin(id);
            
            if (success) {
                return ResponseEntity.ok(Map.of("message", "예약이 성공적으로 승인되었습니다."));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "예약 승인에 실패했습니다. 예약 상태를 확인해주세요."));
            }
            
        } catch (Exception e) {
            log.error("예약 승인 실패 - 예약ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "예약 승인 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "예약 거절", description = "예약을 거절합니다.")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectReservation(@PathVariable Long id) {
        try {
            boolean success = reservationService.rejectReservationByAdmin(id);
            
            if (success) {
                return ResponseEntity.ok(Map.of("message", "예약이 성공적으로 거절되었습니다."));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "예약 거절에 실패했습니다. 예약 상태를 확인해주세요."));
            }
            
        } catch (Exception e) {
            log.error("예약 거절 실패 - 예약ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "예약 거절 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "예약 이용완료 처리", description = "예약을 이용완료 상태로 변경합니다.")
    @PutMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeReservation(@PathVariable Long id) {
        try {
            boolean success = reservationService.completeReservationByAdmin(id);
            
            if (success) {
                return ResponseEntity.ok(Map.of("message", "이용완료 처리가 완료되었습니다."));
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "이용완료 처리에 실패했습니다. 예약 상태를 확인해주세요."));
            }
            
        } catch (Exception e) {
            log.error("이용완료 처리 실패 - 예약ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "이용완료 처리 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "예약 상세 조회", description = "예약 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReservationDetail(@PathVariable Long id) {
        try {
            ReservationDTO reservation = reservationService.getReservationDetailForAdmin(id);
            
            if (reservation != null) {
                return ResponseEntity.ok(Map.of("reservation", reservation));
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("예약 상세 조회 실패 - 예약ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "예약 상세 조회 중 오류가 발생했습니다."));
        }
    }

    @Operation(summary = "예약 통계", description = "예약 상태별 통계를 조회합니다.")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getReservationStats() {
        try {
            Map<String, Object> stats = reservationService.getReservationStatsForAdmin();
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            log.error("예약 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "예약 통계 조회 중 오류가 발생했습니다."));
        }
    }
} 