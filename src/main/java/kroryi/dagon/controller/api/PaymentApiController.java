//package kroryi.dagon.controller;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/payment")
//public class PaymentApiController {
//
//    private final PaymentSerevice paymentSerevice;
//
//    public PaymentApiController(PaymentService paymentService) {
//        this.PaymentSerevice = paymentService;
//    }
//
//    @PostMapping("process")
//    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
//        boolean isSuccessful = paymentSerevice.processPayment(paymentRequest);
//        if (isSuccessful) {
//            return ResponseEntity.ok("결제가 성공적으로 처리되었습니다.");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제가 실패했습니다.");
//        }
//    }
//}
