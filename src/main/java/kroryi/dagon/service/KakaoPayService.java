package kroryi.dagon.service;

import kroryi.dagon.DTO.KakaoPayDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoPayService {

    private static final String HOST = "https://kapi.kakao.com";

    @Value("${kakao.admin}")
    private String kakaoAdminKey;

    private KakaoPayDTO kakaoPayDTO;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(HOST)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String kakaoPayReady() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "goguma");
        params.add("item_name", "비둘기");
        params.add("quantity", "1");
        params.add("total_amount", "20000");
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:9090/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:9090/kakaoPayCancel");
        params.add("fail_url", "http://localhost:9090/kakaoPayFail");

        try {
            kakaoPayDTO = webClient.post()
                    .uri("/v1/payment/ready")
                    .header(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoAdminKey)
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(KakaoPayDTO.class)
                    .block();

            log.info("카카오페이 요청 성공: {}", kakaoPayDTO);
            return kakaoPayDTO != null ? kakaoPayDTO.getNext_redirect_pc_url() : "/pay";

        } catch (Exception e) {
            log.error("카카오페이 요청 실패", e);
            return "/pay";

        }
    }
}
