package kroryi.dagon.controller.legacy.Partner;

import kroryi.dagon.entity.Reservation;
import kroryi.dagon.service.auth.PartnerService;
import kroryi.dagon.service.product.ProductService;
import kroryi.dagon.service.support.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/partner/dashboard")
@RequiredArgsConstructor
@Log4j2
public class ApiPartnerDashboard {
    
    private final PartnerService partnerService;
    private final ProductService productService;
    private final InquiryService inquiryService;

    @GetMapping("/reservation-count")
    public ResponseEntity<Long> getReservationCount() {
        Long partnerId = partnerService.getCurrentPartnerId();
        Long count = partnerService.getReservationCountByPartnerId(partnerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/product-count")
    public ResponseEntity<Long> getProductCount() {
        Long partnerId = partnerService.getCurrentPartnerId();
        Long count = productService.getProductCountByPartnerId(partnerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/today-reservation-count")
    public ResponseEntity<Long> getTodayReservationCount() {
        Long partnerId = partnerService.getCurrentPartnerId();
        Long count = partnerService.getTodayReservationCountByPartnerId(partnerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/unanswered-inquiry-count")
    public ResponseEntity<Long> getUnansweredInquiryCount() {
        Long partnerId = partnerService.getCurrentPartnerId();
        Long count = inquiryService.getUnansweredInquiryCount(partnerId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/recent-reservations")
    public ResponseEntity<List<Map<String, Object>>> getRecentReservations() {
        Long partnerId = partnerService.getCurrentPartnerId();
        log.info("현재 파트너 ID: {}", partnerId);
        
        List<Reservation> reservations = partnerService.getRecentReservationsByPartnerId(partnerId);
        log.info("조회된 예약 수: {}", reservations.size());
        
        List<Map<String, Object>> response = reservations.stream()
            .map(reservation -> {
                Map<String, Object> map = new HashMap<>();
                map.put("reservationId", reservation.getReservationId());
                map.put("createdAt", reservation.getCreatedAt());
                map.put("fishingAt", reservation.getFishingAt());
                map.put("numPerson", reservation.getNumPerson());
                map.put("reservationStatus", reservation.getReservationStatus());
                map.put("userName", reservation.getUser().getUname());
                map.put("productName", reservation.getProduct().getProdName());
                return map;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
}
