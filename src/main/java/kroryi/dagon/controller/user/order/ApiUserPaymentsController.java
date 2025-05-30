package kroryi.dagon.controller.user.order;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.service.PaymentsService;
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
        String impUid = (String) body.get("imp_uid");
        log.info("Verifying payment with imp_uid-->{}", impUid);

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
        Payment payment = iamportResponse.getResponse();

        // 실제 결제금액 서버에서 확인
        if (payment.getAmount().intValue() == 100) {
            // DB 저장
            paymentsService.savePayment(payment);
            return ResponseEntity.ok(Map.of("success", "true"));
        } else {
            return ResponseEntity.status(400).body(Map.of("success", "false", "message", "금액 불일치"));
        }
    }
}
