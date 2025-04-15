//package kroryi.dagon.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentController {
//
//    private final PaymentSerevice paymentSerevice;
//
//    public PaymentController(PaymentService paymentService) {
//        this.paymentSerevice = paymentService;
//    }
//
//    @PostMapping("process")
//    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
//        boolean isSuccessful = paymentSerevice.processPayment(paymentRequest);
//        if (isSuccessful) {
//            return ResponseEntity.ok("결제가 성공적으로 처리되었습니다.");
//        } else {
//            return ResponseEntity.status(KttpStatus.BAD_REQUEST).body("결제가 실패했습니다.");
//        }
//    }
//}
