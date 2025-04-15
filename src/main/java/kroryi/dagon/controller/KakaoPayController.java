package kroryi.dagon.controller;

import kroryi.dagon.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Log4j2
public class KakaoPayController {

    @Setter(onMethod_ = @Autowired)
    private KakaoPayService kakaoPay;

    @GetMapping("/kakaoPay")
    public String kakaoPayGet() {
        log.info("1111111111111111111111");

        return "kakaopay/kakaopay";
    }

    @PostMapping("/kakaoPay")
    public String kakaoPay() {
        log.info("kakaoPay post.....................");

        return "redirect:" + kakaoPay.kakaoPayReady();
    }

    @GetMapping("/kakaoPaySuccess")
    public String kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
        log.info("kakaoPay Success get................");
        log.info("kakaoPaySuccess pg_token : {}", pg_token);
        model.addAttribute("pg_token", pg_token);
        return "kakaopay/kakaoPaySuccess";  // 템플릿 반환
    }

    @GetMapping("/kakaoPayCancel")
    public String kakaoPayCancel(Model model) {
        log.info("kakaoPay Cancel get................");
        // 취소된 결제에 대한 처리를 여기에 작성합니다.
        return "kakaopay/kakaoPayCancel";  // 템플릿 반환
    }

    @GetMapping("/kakaoPayFail")
    public String kakaoPayFail(Model model) {
        log.info("kakaoPay Fail get................");
        // 실패한 결제에 대한 처리를 여기에 작성합니다.
        return "kakaopay/kakaoPayFail";  // 템플릿 반환
    }
}
