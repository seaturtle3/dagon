package kroryi.dagon.service.order;

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
}

