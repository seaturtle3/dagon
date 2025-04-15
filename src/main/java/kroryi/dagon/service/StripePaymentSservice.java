//package kroryi.dagon.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class StripePaymentSservice {
//
//    public StripePaymentSservice() {
//        // Stripe API 키 설정
//        Stripe.apiKey = "sk_test_your_api_key";
//    }
//
//    public boolean processPayment(String token, int amount) {
//        Map<String, Object> chargeParams = new HashMap<>();
//        chargeParams.put("amount", amount);
//        chargeParams.put("currency", "usd");
//        chargeParams.put("source", token);
//
//        try {
//            Charge charge = Charge.create(chargeParams);
//            return charge.getPaid();
//        } catch (StripeException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
