package kroryi.dagon.service;

import jakarta.persistence.criteria.Predicate;
import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.ReservationStatus;
import kroryi.dagon.enums.SubType;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.SeaFreshwaterFishingRepository;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeaFreshwaterFishingService {

    private final SeaFreshwaterFishingRepository seaFreshwaterFishingRepository;
    private final ProductRepository productRepository;

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
        // 유저, 상품, 옵션 등 엔티티 매핑 생략
        reservation.setFishingAt(dto.getFishingAt());
        reservation.setNumPerson(dto.getNumPerson());
        reservation.setReservationStatus(ReservationStatus.PENDING);
        reservation.setPaymentsMethod(dto.getPaymentsMethod());

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
            dto.setCreatedAt(product.getCreatedAt());  // LocalDateTime에서 LocalDate만 추출
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
                .fishingAt(reservation.getFishingAt())
                .numPerson(reservation.getNumPerson())
                .reservationStatus(reservation.getReservationStatus())
                .paymentsMethod(reservation.getPaymentsMethod())
                .paidAt(reservation.getPaidAt())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public boolean cancelReservationByUser(Long reservationId, Long uno) {
        Reservation reservation = seaFreshwaterFishingRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 해당 예약이 로그인한 사용자 소유인지 확인
        if (!reservation.getUser().getUno().equals(uno)) {
            return false; // 본인의 예약이 아님
        }

        // 예약 상태를 취소로 변경
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

    private ReservationDTO toDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .reservationId(reservation.getReservationId())
                .productName(reservation.getProduct().getProdName())
                .optionName(reservation.getProductOption().getOptName())
                .userName(reservation.getUser().getUname())
                .fishingAt(reservation.getFishingAt())
                .numPerson(reservation.getNumPerson())
                .reservationStatus(reservation.getReservationStatus())
                .paymentsMethod(reservation.getPaymentsMethod())
                .paidAt(reservation.getPaidAt())
                .createdAt(reservation.getCreatedAt())
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
}

