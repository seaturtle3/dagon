package kroryi.dagon.controller.admin.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kroryi.dagon.DTO.PaymentsDTO;
import kroryi.dagon.service.order.PaymentsCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Payments", description = "결제정보 관리 API (관리자)")
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class ApiAdminPaymentsController {

    private final PaymentsCrudService paymentsCrudService;

    @GetMapping
    @Operation(summary = "전체 결제정보 조회", description = "KG이니시스 전체 결제정보 조회")
    public List<PaymentsDTO> getAllPayments() {
        return paymentsCrudService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "결제정보 읽기", description = "KG이니시스 결제정보 읽기")
    public PaymentsDTO getPayments(@PathVariable Long id) {
        return paymentsCrudService.findById(id);
    }

    @PostMapping
    @Operation(summary = "결제정보 생성", description = "KG이니시스 결제정보 생성")
    public PaymentsDTO createPayment(@RequestBody PaymentsDTO dto) {
        return paymentsCrudService.save(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "결제정보 업데이트", description = "KG이니시스 결제정보 업데이트")
    public PaymentsDTO updatePayment(@PathVariable Long id, @RequestBody PaymentsDTO dto) {
        return paymentsCrudService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "결제정보 삭제", description = "KG이니시스 결제정보 삭제")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentsCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
