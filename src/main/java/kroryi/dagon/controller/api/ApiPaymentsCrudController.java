package kroryi.dagon.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import kroryi.dagon.DTO.PaymentsDTO;
import kroryi.dagon.service.PaymentsCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class ApiPaymentsCrudController {

    private final PaymentsCrudService paymentsCrudService;

    @GetMapping
    public List<PaymentsDTO> getAllPayments() {
        return paymentsCrudService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "결제 정보 조회", description = "결제 정보 조회")
    public PaymentsDTO getPayments(@PathVariable Long id) {
        return paymentsCrudService.findById(id);
    }

    @PostMapping
    @Operation(summary = "결제 정보 생성", description = "결제 정보 생성")
    public PaymentsDTO createPayment(@RequestBody PaymentsDTO dto) {
        return paymentsCrudService.save(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "결제 정보 업데이트", description = "결제 정보 업데이트")
    public PaymentsDTO updatePayment(@PathVariable Long id, @RequestBody PaymentsDTO dto) {
        return paymentsCrudService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "결제 정보 삭제", description = "결제 정보 삭제")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentsCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
