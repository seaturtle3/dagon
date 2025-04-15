//package kroryi.dagon.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//
//    public void createOrder(Order order, Payment payment) throws Exception {
//        try {
//            // 주문 저장
//            orderRepository.save(order);
//
//            // 결제 정보 저장
//            paymentRepository.save(payment);
//
//            // 외부 결제 게이트웨이 호출 (예: stripe)
//            boolean paymentSuccess = processExternalPayment(order.getI(), payment.getAmoun());
//
//            if (!paymentSuccess) {
//                throw new Exception("결제가 실패했습니다.");
//            }
//
//            // 주문 상태 업데이트
//            order.setStatus("COMPLETED");
//            orderRepository.save(order);
//
//        } catch (Exception e) {
//            // 트랜잭션 롤백
//            throw new Exception("주문 생성 중 오류 발생: " + e.getMessage());
//        }
//
//    }
//}
