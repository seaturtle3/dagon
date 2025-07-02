package kroryi.dagon.service.order;

import kroryi.dagon.DTO.product.ProductDTO;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.entity.PaymentsEntity;
import kroryi.dagon.entity.User;
import kroryi.dagon.entity.product.Product;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.entity.product.ProductOption;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.ReservationStatus;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.PaymentsRepository;
import kroryi.dagon.repository.UserRepository;
import kroryi.dagon.repository.product.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import kroryi.dagon.repository.product.ProductOptionRepository;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SeaFreshwaterFishingService {

    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;
    private final PaymentsRepository paymentsRepository;

    public String getFindAll() {
        return seaFreshwaterFishingRepository.findAll().toString();
    }

    // 모든 상품을 지역과 메인타입으로 필터
    public List<ProductDTO> getAllProductsByRegionAndMainType(ProdRegion region, MainType mainType) {
        if (region == null) {
            return productRepository.findByMainType(mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        } else {
            return productRepository.findByProdRegionAndMainType(region, mainType)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }
    }

    public ReservationDTO createReservation(ReservationDTO dto) {
        Reservation reservation = new Reservation();
        // 유저, 상품, 옵션 등 엔티티 매핑
        reservation.setFishingAt(dto.getFishingAt());
        reservation.setNumPerson(dto.getNumPerson());
        reservation.setOptionQuantity(dto.getOptionQuantity() != null ? dto.getOptionQuantity() : 1);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setPaymentsMethod(dto.getPaymentsMethod());
        // userId로 User 엔티티 조회
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다: " + dto.getUserId()));
        reservation.setUser(user);
        // Product 엔티티 조회 후 할당
        Long prodId = dto.getProdId();

        log.info("------------------ prodid: {}" ,prodId);
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + prodId));
        reservation.setProduct(product);
        // ProductOption 엔티티 조회 후 할당
        Long optionId = dto.getOptionId();
        log.info("옵션 ID 조회: {}", optionId);
        ProductOption productOption = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다. id=" + optionId));
        reservation.setProductOption(productOption);
        
        log.info("조회된 옵션 정보: ID={}, 이름={}, 가격={}", 
                productOption.getOptId(), 
                productOption.getOptName(), 
                productOption.getPrice());

        // 금액 계산 (상품가격 * 인원수 + 옵션가격 * 옵션수량)
        BigDecimal totalAmount = BigDecimal.ZERO;
        Integer numPerson = dto.getNumPerson();
        Integer optionQuantity = dto.getOptionQuantity() != null ? dto.getOptionQuantity() : 1;
        
        // 상품 가격 계산
        if (product.getProdPrice() != null && numPerson != null) {
            BigDecimal productAmount = product.getProdPrice().multiply(BigDecimal.valueOf(numPerson));
            totalAmount = totalAmount.add(productAmount);
            log.info("상품 가격 계산: {} * {} = {}", product.getProdPrice(), numPerson, productAmount);
        } else {
            // 상품 가격이 없으면 기본 가격 사용
            BigDecimal defaultProductAmount = BigDecimal.valueOf(numPerson * 30000); // 기본 3만원
            totalAmount = totalAmount.add(defaultProductAmount);
            log.warn("상품 가격이 없어 기본 가격 사용: 인원수={}, 기본상품가격={}", numPerson, defaultProductAmount);
        }
        
        // 옵션 가격 계산
        if (productOption.getPrice() != null) {
            BigDecimal optionAmount = productOption.getPrice().multiply(BigDecimal.valueOf(optionQuantity));
            totalAmount = totalAmount.add(optionAmount);
            log.info("옵션 가격 계산: {} * {} = {}", productOption.getPrice(), optionQuantity, optionAmount);
        } else {
            // 옵션 가격이 없으면 기본 가격 사용
            BigDecimal defaultOptionAmount = BigDecimal.valueOf(optionQuantity * 20000); // 기본 2만원
            totalAmount = totalAmount.add(defaultOptionAmount);
            log.warn("옵션 가격이 없어 기본 가격 사용: 옵션수량={}, 기본옵션가격={}", optionQuantity, defaultOptionAmount);
        }
        
        reservation.setAmount(totalAmount);
        log.info("총 예약 금액: {}", totalAmount);

        // 결제 정보 처리
        PaymentsEntity payment = null;
        if (dto.getPaymentId() != null && !dto.getPaymentId().trim().isEmpty()) {
            try {
                // paymentId가 숫자인 경우 (PaymentsEntity의 id)
                if (dto.getPaymentId().matches("\\d+")) {
                    Long paymentId = Long.parseLong(dto.getPaymentId());
                    payment = paymentsRepository.findById(paymentId)
                        .orElse(null);
                } else {
                    // paymentId가 impUid인 경우
                    payment = paymentsRepository.findByImpUid(dto.getPaymentId())
                        .orElse(null);
                }
            } catch (Exception e) {
                // 결제 정보 조회 실패 시 로그만 남기고 계속 진행
                System.err.println("결제 정보 조회 실패: " + dto.getPaymentId() + ", 오류: " + e.getMessage());
            }
        }
        
        // 결제 정보가 있으면 설정, 없으면 null로 유지
        reservation.setPayment(payment);

        Reservation saved = seaFreshwaterFishingRepository.save(reservation);
        return toDTO(saved);
    }

    // 필터 : 메인타입, 서브타입, 지역
    public List<Product> getProductsByFilters(MainType mainType, SubType subType, ProdRegion region) {
        if (subType == null && region == null) {
            return productRepository.findByMainType(mainType);
        } else if (subType == null) {
            return productRepository.findByMainTypeAndProdRegion(mainType, region);
        } else if (region == null) {
            return productRepository.findByMainTypeAndSubType(mainType, subType);
        } else {
            return productRepository.findByMainTypeAndSubTypeAndProdRegion(mainType, subType, region);
        }
    }

    public ReservationDTO getReservationById(Long id) {
        return seaFreshwaterFishingRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdId(product.getProdId());
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setMainType(product.getMainType());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        // LocalDateTime -> LocalDate 변환
        if (product.getCreatedAt() != null) {
            dto.setCreatedAt(product.getCreatedDate());  // LocalDateTime에서 LocalDate만 추출
        }

        return dto;
    }


    public List<ReservationDTO> getReservationsByUserId(Long uid) {
        List<Reservation> seafreshwatergRepository = seaFreshwaterFishingRepository. findByUser_Uno(uid);
        return seafreshwatergRepository.stream().map(this::convertDTO).collect(Collectors.toList());
    }

    private ReservationDTO convertDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .reservationId(reservation.getReservationId())
                .productName(reservation.getProduct().getProdName())
                .optionName(reservation.getProductOption().getOptName())
                .userName(reservation.getUser() != null ? reservation.getUser().getUname() : null)
                .fishingAt(reservation.getFishingAt())
                .numPerson(reservation.getNumPerson())
                .optionQuantity(reservation.getOptionQuantity())
                .amount(reservation.getAmount())
                .reservationStatus(reservation.getReservationStatus())
                .paymentsMethod(reservation.getPaymentsMethod())
                .paidAt(reservation.getPaidAt())
                .createdAt(reservation.getCreatedAt())
                .prodId(reservation.getProduct() != null ? reservation.getProduct().getProdId() : null)
                .optionId(reservation.getProductOption() != null ? reservation.getProductOption().getOptId() : null)
                .userId(reservation.getUser() != null ? reservation.getUser().getUno() : null)
                .paymentId(String.valueOf(reservation.getPayment() != null ? reservation.getPayment().getId() : null))
                .build();
    }

    public boolean cancelReservationByUser(Long reservationId, Long uno) {
        Reservation reservation = seaFreshwaterFishingRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        Long userUno = reservation.getUser().getUno();
        Long partnerUno = reservation.getProduct().getPartner().getUno();

        // 예약 소유자가 uno이거나, 상품의 파트너가 uno일 경우에만 취소 허용
        if (!userUno.equals(uno) && !partnerUno.equals(uno)) {
            return false; // 본인의 예약도 아니고 본인 상품도 아님
        }

        reservation.setReservationStatus(ReservationStatus.CANCELED);
        seaFreshwaterFishingRepository.save(reservation);

        return true;
    }

    public Page<ReservationDTO> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = seaFreshwaterFishingRepository.findAllWithDetails(pageable);
        return reservations.map(this::toDTO);
    }

    public ReservationDTO updateReservation(Long id, ReservationDTO dto) {
        Reservation reservation = seaFreshwaterFishingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 유효성 체크 및 업데이트
        reservation.setFishingAt(dto.getFishingAt());
        reservation.setNumPerson(dto.getNumPerson());
        reservation.setPaymentsMethod(dto.getPaymentsMethod());
        reservation.setReservationStatus(dto.getReservationStatus());

        Reservation updated = seaFreshwaterFishingRepository.save(reservation);
        return toDTO(updated);
    }

    // 관리자 예약 취소
    public boolean cancelReservationByAdmin(Long reservationId) {
        // 예약 ID로 예약 찾기
        Optional<Reservation> optional = seaFreshwaterFishingRepository.findById(reservationId);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();

            // 예약 상태 확인 (이미 취소된 건지 확인)
            if (reservation.getReservationStatus() != ReservationStatus.CANCELED) {
                reservation.setReservationStatus(ReservationStatus.CANCELED);
                seaFreshwaterFishingRepository.save(reservation);
                return true;
            }
        }
        return false;
    }

    public List<ReservationDTO> getReservationsByUno(Long uno) {
        List<Reservation> reservations = seaFreshwaterFishingRepository.findByUser_Uno(uno);
        return reservations.stream()
                .map(this::toDTO) // 인스턴스 메서드 참조
                .collect(Collectors.toList());
    }


    private ReservationDTO toDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .reservationId(reservation.getReservationId())
                .productName(reservation.getProduct().getProdName())
                .optionName(reservation.getProductOption().getOptName())
                .userName(reservation.getUser() != null ? reservation.getUser().getUname() : null)
                .fishingAt(reservation.getFishingAt())
                .numPerson(reservation.getNumPerson())
                .optionQuantity(reservation.getOptionQuantity())
                .amount(reservation.getAmount())
                .reservationStatus(reservation.getReservationStatus())
                .paymentsMethod(reservation.getPaymentsMethod())
                .paidAt(reservation.getPaidAt())
                .createdAt(reservation.getCreatedAt())
                .prodId(reservation.getProduct() != null ? reservation.getProduct().getProdId() : null)
                .optionId(reservation.getProductOption() != null ? reservation.getProductOption().getOptId() : null)
                .userId(reservation.getUser() != null ? reservation.getUser().getUno() : null)
                .paymentId(String.valueOf(reservation.getPayment() != null ? reservation.getPayment().getId() : null))
                .phone(reservation.getUser().getPhone())
                .email(reservation.getUser().getEmail())
                .build();
    }


    public Page<ReservationDTO> searchReservations(String searchType, String keyword, Pageable pageable) {
        if ("productName".equalsIgnoreCase(searchType)) {
            Page<Reservation> reservations = seaFreshwaterFishingRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
            return reservations.map(this::toDTO);
        } else if ("userName".equalsIgnoreCase(searchType)) {
            Page<Reservation> reservations = seaFreshwaterFishingRepository.findByUserNameContainingIgnoreCase(keyword, pageable);
            return reservations.map(this::toDTO);
        } else {
            // 기본 검색 안하면 전체 리턴
            return getAllReservations(pageable);
        }
    }

    public List<Reservation> getReservationsByUserUno(Long uno) {
        return seaFreshwaterFishingRepository.findByUser_Uno(uno);
    }

    public boolean cancelReservationByPartner(Long reservationId, Long partnerUno) {
        Optional<Reservation> resOpt = seaFreshwaterFishingRepository.findById(reservationId);
        if (resOpt.isEmpty()) return false;

        Reservation res = resOpt.get();

        // 파트너가 관리하는 상품인지 확인
        if (!res.getProduct().getPartner().getUno().equals(partnerUno)) {
            return false; // 자기 상품이 아닌 예약이라면 취소 불가
        }

        // 이미 취소된 예약인지 확인
        if (res.getReservationStatus() == ReservationStatus.CANCELED) {
            return false;
        }

        // 취소 처리
        res.setReservationStatus(ReservationStatus.CANCELED);
        seaFreshwaterFishingRepository.save(res);
        return true;
    }

    public List<Reservation> getReservationsByPartnerUno(Long partnerUno) {
        return seaFreshwaterFishingRepository.findAllByProduct_Partner_Uno(partnerUno);
    }


    public ReservationDTO getReservationDetail(Long reservationId) {
        Reservation reservation = seaFreshwaterFishingRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약 정보를 찾을 수 없습니다."));

        return ReservationDTO.builder()
                .userId(reservation.getUser() != null ? reservation.getUser().getUno() : null)
                .reservationId(reservation.getReservationId())
                .productName(reservation.getProduct().getProdName())
                .optionName(reservation.getProductOption().getOptName())
                .userName(reservation.getUser() != null ? reservation.getUser().getUname() : null)
                .fishingAt(reservation.getFishingAt())
                .numPerson(reservation.getNumPerson())
                .optionQuantity(reservation.getOptionQuantity())
                .amount(reservation.getAmount())
                .reservationStatus(reservation.getReservationStatus())
                .paymentsMethod(reservation.getPaymentsMethod())
                .paidAt(reservation.getPaidAt())
                .createdAt(reservation.getCreatedAt())
                .prodId(reservation.getProduct() != null ? reservation.getProduct().getProdId() : null)
                .optionId(reservation.getProductOption() != null ? reservation.getProductOption().getOptId() : null)
                .paymentId(String.valueOf(reservation.getPayment() != null ? reservation.getPayment().getId() : null))
                .email(reservation.getUser().getEmail())
                .phone(reservation.getUser().getPhone())
                .build();
    }

    // ========== 관리자용 메서드들 ==========

    /**
     * 관리자용 전체 예약 조회
     */
    public Page<ReservationDTO> getAllReservationsForAdmin(Pageable pageable) {
        Page<Reservation> reservations = seaFreshwaterFishingRepository.findAllWithDetails(pageable);
        return reservations.map(this::toDTO);
    }

    /**
     * 관리자용 검색 조건별 예약 조회
     */
    public Page<ReservationDTO> searchReservationsForAdmin(String search, Pageable pageable) {
        // 예약번호, 회원명, 파트너명으로 검색
        Page<Reservation> reservations = seaFreshwaterFishingRepository.findBySearchKeywordForAdmin(search, pageable);
        return reservations.map(this::toDTO);
    }

    /**
     * 관리자용 날짜별 예약 조회
     */
    public Page<ReservationDTO> getReservationsByDateForAdmin(LocalDate date, Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        
        Page<Reservation> reservations = seaFreshwaterFishingRepository.findByFishingAtBetweenForAdmin(startOfDay, endOfDay, pageable);
        return reservations.map(this::toDTO);
    }

    /**
     * 관리자용 상태별 예약 조회
     */
    public Page<ReservationDTO> getReservationsByStatusForAdmin(String status, Pageable pageable) {
        ReservationStatus reservationStatus = convertStatusString(status);
        Page<Reservation> reservations = seaFreshwaterFishingRepository.findByReservationStatusForAdmin(reservationStatus, pageable);
        return reservations.map(this::toDTO);
    }

    /**
     * 관리자용 예약 승인
     */
    public boolean approveReservationByAdmin(Long reservationId) {
        Optional<Reservation> optional = seaFreshwaterFishingRepository.findById(reservationId);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            
            // 예약 대기 상태인 경우에만 승인 가능
            if (reservation.getReservationStatus() == ReservationStatus.PENDING) {
                reservation.setReservationStatus(ReservationStatus.PAID);
                reservation.setPaidAt(LocalDateTime.now());
                seaFreshwaterFishingRepository.save(reservation);
                return true;
            }
        }
        return false;
    }

    /**
     * 관리자용 예약 거절
     */
    public boolean rejectReservationByAdmin(Long reservationId) {
        Optional<Reservation> optional = seaFreshwaterFishingRepository.findById(reservationId);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            
            // 예약 대기 상태인 경우에만 거절 가능
            if (reservation.getReservationStatus() == ReservationStatus.PENDING) {
                reservation.setReservationStatus(ReservationStatus.CANCELED);
                seaFreshwaterFishingRepository.save(reservation);
                return true;
            }
        }
        return false;
    }

    /**
     * 관리자용 이용완료 처리
     */
    public boolean completeReservationByAdmin(Long reservationId) {
        Optional<Reservation> optional = seaFreshwaterFishingRepository.findById(reservationId);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            
            // 예약 확정 상태인 경우에만 이용완료 처리 가능
            if (reservation.getReservationStatus() == ReservationStatus.PAID) {
                reservation.setReservationStatus(ReservationStatus.COMPLETED);
                seaFreshwaterFishingRepository.save(reservation);
                return true;
            }
        }
        return false;
    }

    /**
     * 관리자용 예약 상세 조회
     */
    public ReservationDTO getReservationDetailForAdmin(Long reservationId) {
        return getReservationDetail(reservationId);
    }

    /**
     * 관리자용 예약 통계
     */
    public Map<String, Object> getReservationStatsForAdmin() {
        Map<String, Object> stats = new HashMap<>();
        
        // 전체 예약 수
        long totalReservations = seaFreshwaterFishingRepository.count();
        
        // 상태별 예약 수
        long pendingCount = seaFreshwaterFishingRepository.countByReservationStatus(ReservationStatus.PENDING);
        long paidCount = seaFreshwaterFishingRepository.countByReservationStatus(ReservationStatus.PAID);
        long canceledCount = seaFreshwaterFishingRepository.countByReservationStatus(ReservationStatus.CANCELED);
        long completedCount = seaFreshwaterFishingRepository.countByReservationStatus(ReservationStatus.COMPLETED);
        
        // 오늘 예약 수
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1).minusNanos(1);
        long todayCount = seaFreshwaterFishingRepository.countByFishingAtBetween(startOfToday, endOfToday);
        
        stats.put("totalReservations", totalReservations);
        stats.put("pendingCount", pendingCount);
        stats.put("paidCount", paidCount);
        stats.put("canceledCount", canceledCount);
        stats.put("completedCount", completedCount);
        stats.put("todayCount", todayCount);
        
        return stats;
    }

    /**
     * 상태 문자열을 ReservationStatus enum으로 변환
     */
    private ReservationStatus convertStatusString(String status) {
        switch (status) {
            case "예약대기":
            case "PENDING":
                return ReservationStatus.PENDING;
            case "예약확정":
            case "PAID":
                return ReservationStatus.PAID;
            case "예약취소":
            case "CANCELED":
                return ReservationStatus.CANCELED;
            case "이용완료":
            case "COMPLETED":
                return ReservationStatus.COMPLETED;
            default:
                return ReservationStatus.PENDING;
        }
    }

}

