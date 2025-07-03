package kroryi.dagon.controller.user.order;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.order.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "Payments", description = "아임포트 결제 검증 API (사용자)")
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Log4j2
public class ApiUserPaymentsController {

    private final IamportClient iamportClient;
    private final PaymentsService paymentsService;

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody LinkedHashMap<String, Object> body)
            throws IamportResponseException, IOException {
        String impUid = (String) body.get("impUid");
        log.info("Verifying payment with imp_uid-->{}", impUid);

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = iamportResponse.getResponse();

        // 실제 결제금액 서버에서 확인
        if (payment.getAmount().intValue() == (int) body.get("amount")) {
            // DB 저장하고 저장된 PK 반환
            Long paymentId = paymentsService.savePayment(payment);
            
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("success", true);
            response.put("paymentId", paymentId); // 저장된 결제 PK 반환
            response.put("impUid", impUid);
            response.put("amount", payment.getAmount());
            response.put("status", payment.getStatus());
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(400).body(Map.of("success", false, "message", "금액 불일치"));
        }
    }
}
