package kroryi.dagon.service;

import kroryi.dagon.DTO.PaymentsDTO;
import kroryi.dagon.entity.PaymentsEntity;
import kroryi.dagon.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentsCrudService {

    private final PaymentsRepository paymentsRepository;

    public List<PaymentsDTO> findAll() {
        return paymentsRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public PaymentsDTO findById(Long id) {
        PaymentsEntity entity = paymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));
        return toDto(entity);
    }

    public PaymentsDTO save(PaymentsDTO dto) {
        PaymentsEntity entity = paymentsRepository.save(toEntity(dto));
        return toDto(entity);
    }

    public PaymentsDTO update(Long id, PaymentsDTO dto) {
        PaymentsEntity existing = paymentsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));

        existing.setAmount(dto.getAmount());
        existing.setBuyerName(dto.getBuyerName());
        existing.setStatus(dto.getStatus());
        existing.setPayMethod(dto.getPayMethod());
        existing.setPaidAt(dto.getPaidAt());

        return toDto(paymentsRepository.save(existing));
    }

    public void delete(Long id) {
        paymentsRepository.deleteById(id);
    }

    private PaymentsDTO toDto(PaymentsEntity e) {
        return PaymentsDTO.builder()
                .id(e.getId())
                .impUid(e.getImpUid())
                .merchantUid(e.getMerchantUid())
                .amount(e.getAmount())
                .buyerName(e.getBuyerName())
                .status(e.getStatus())
                .payMethod(e.getPayMethod())
                .paidAt(e.getPaidAt())
                .build();
    }

    private PaymentsEntity toEntity(PaymentsDTO d) {
        return PaymentsEntity.builder()
                .id(d.getId())
                .impUid(d.getImpUid())
                .merchantUid(d.getMerchantUid())
                .amount(d.getAmount())
                .buyerName(d.getBuyerName())
                .status(d.getStatus())
                .payMethod(d.getPayMethod())
                .paidAt(d.getPaidAt())
                .build();
    }
}
