package kroryi.dagon.service.order;

import com.siot.IamportRestClient.response.Payment;
import kroryi.dagon.entity.PaymentsEntity;
import kroryi.dagon.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;

    public void savePayment(Payment payment) {
        PaymentsEntity entity = PaymentsEntity.builder()
                .impUid(payment.getImpUid())
                .merchantUid(payment.getMerchantUid())
                .amount(payment.getAmount())
                .buyerName(payment.getBuyerName())
                .status(payment.getStatus())
                .payMethod(payment.getPayMethod())
                .paidAt(payment.getPaidAt()
                        .toInstant()
                        .atZone(ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime())
                .build();

        paymentsRepository.save(entity);
    }
}
