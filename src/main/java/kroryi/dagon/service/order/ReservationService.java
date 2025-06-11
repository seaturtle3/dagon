package kroryi.dagon.service.order;

import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.enums.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReservationService {

    private static final List<ReservationDTO> reservations = new ArrayList<>();

    // 예약 생성 (Create)
    public static boolean createReservation(Long userId, ReservationDTO reservationDTO) {
        reservationDTO.setReservationId((long) (reservations.size() + 1));
        reservationDTO.setCreatedAt(LocalDateTime.now());
        reservationDTO.setReservationStatus(ReservationStatus.PENDING);
        reservations.add(reservationDTO);
        log.info("Reservation created: {}", reservationDTO);
        return true;
    }

    // 모든 예약 조회 (Read)
    public List<ReservationDTO> getAllReservations() {
        return reservations;
    }

    // 특정 사용자의 예약 조회 (Read)
    public List<ReservationDTO> getReservationsByUserId(Long userId) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationId().equals(userId))
                .toList();
    }

    // 특정 예약 조회 (Read)
    public ReservationDTO getReservationById(Long reservationId) {
        Optional<ReservationDTO> reservation = reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();
        return reservation.orElse(null);
    }

    // 예약 수정 (Update)
    public static boolean updateReservation(Long reservationId, Long userId, ReservationDTO updatedReservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservationId)) {
                updatedReservation.setReservationId(reservationId);
                updatedReservation.setCreatedAt(reservations.get(i).getCreatedAt());
                reservations.set(i, updatedReservation);
                log.info("Reservation updated: {}", updatedReservation);
                return true;
            }
        }
        return false;
    }

    // 예약 삭제 (Delete)
    public static boolean deleteReservation(Long reservationId, Long userId) {
        boolean removed = reservations.removeIf(reservation -> reservation.getReservationId().equals(reservationId));
        if (removed) {
            log.info("Reservation deleted: {}", reservationId);
            return true;
        } else {
            return false;
        }
    }

    // 예약 상태 변경
    public boolean changeReservationStatus(Long reservationId, ReservationStatus newStatus) {
        Optional<ReservationDTO> reservation = reservations.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();

        if (reservation.isPresent()) {
            reservation.get().setReservationStatus(newStatus);
            log.info("Reservation status changed: {} -> {}", reservationId, newStatus);
            return true;
        } else {
            log.warn("Reservation not found for status update: {}", reservationId);
            return false;
        }
    }

    // 예약이 유효한지 검증
    public boolean isValidReservation(Long reservationId) {
        return reservations.stream().anyMatch(r -> r.getReservationId().equals(reservationId));
    }

    // 예약 목록을 예약 날짜 순으로 정렬하여 반환
    public List<ReservationDTO> getSortedReservations() {
        return reservations.stream()
                .sorted(Comparator.comparing(ReservationDTO::getFishingAt))
                .toList();
    }
}
