package kroryi.dagon.service;

import kroryi.dagon.DTO.ProductDTO;
import kroryi.dagon.DTO.ReservationDTO;
import kroryi.dagon.entity.Product;
import kroryi.dagon.entity.Reservation;
import kroryi.dagon.enums.MainType;
import kroryi.dagon.enums.ProdRegion;
import kroryi.dagon.enums.ReservationStatus;
import kroryi.dagon.repository.ProductRepository;
import kroryi.dagon.repository.ReservationRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeaFreshwaterFishingService {

    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public String getFindAll(){
        return reservationRepository.findAll().toString();
    }

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

    public ProductDTO getProductById(Long prodId) {
        Product product = productRepository.findById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + prodId));

        // Product를 ProductDTO로 변환하여 리턴
        return convertToDTO(product);
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProdId(product.getProdId());
        dto.setProdName(product.getProdName());
        dto.setProdRegion(product.getProdRegion());
        dto.setMainType(product.getMainType());
        dto.setSubType(product.getSubType());
        dto.setMaxPerson(product.getMaxPerson());
        dto.setMinPerson(product.getMinPerson());
        dto.setWeight(product.getWeight());
        dto.setProdAddress(product.getProdAddress());
        dto.setProdDescription(product.getProdDescription());
        dto.setProdEvent(product.getProdEvent());
        dto.setProdNotice(product.getProdNotice());
        return dto;
    }


    public List<ReservationDTO> getReservationsByUserId(Long uid) {
        List<Reservation> reservations = reservationRepository. findByUser_Uno(uid);
        return reservations.stream().map(this::convertDTO).collect(Collectors.toList());
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
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 해당 예약이 로그인한 사용자 소유인지 확인
        if (!reservation.getUser().getUno().equals(uno)) {
            return false; // 본인의 예약이 아님
        }

        // 예약 상태를 취소로 변경
        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);

        return true;
    }
}
