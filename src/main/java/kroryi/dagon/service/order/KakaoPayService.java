package kroryi.dagon.service.order;

import kroryi.dagon.DTO.KakaoPayDTO;
import kroryi.dagon.entity.KakaoPayEntity;
import kroryi.dagon.repository.KakaoPayRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoPayService {

    private final KakaoPayRepository kakaoPayRepository;

    private static final String HOST = "https://kapi.kakao.com";

    public KakaoPayDTO save(KakaoPayDTO dto) {
        KakaoPayEntity entity = KakaoPayEntity.fromDTO(dto);
        KakaoPayEntity saved = kakaoPayRepository.save(entity);
        return saved.toDTO();
    }

    public KakaoPayDTO get(Long id) {
        KakaoPayEntity entity = kakaoPayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("결제 정보가 없습니다."));
        return entity.toDTO();
    }

    public List<KakaoPayDTO> getAll() {
        return kakaoPayRepository.findAll().stream()
                .map(KakaoPayEntity::toDTO)
                .collect(Collectors.toList());
    }

    public KakaoPayDTO update(Long id, KakaoPayDTO dto) {
        KakaoPayEntity entity = kakaoPayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("수정할 결제 정보가 없습니다."));

        entity.updateFromDTO(dto);
        KakaoPayEntity updated = kakaoPayRepository.save(entity);
        return updated.toDTO();
    }

    public void delete(Long id) {
        kakaoPayRepository.deleteById(id);
    }

    @Value("${kakao.admin}")
    private String kakaoAdminKey;

    private KakaoPayDTO kakaoPayDTO;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(HOST)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Value("${app.server.base-url}")
    private String baseUrl;

    public String kakaoPayReady() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "goguma");
        params.add("item_name", "비둘기");
        params.add("quantity", "1");
        params.add("total_amount", "20000");
        params.add("tax_free_amount", "100");
        params.add("approval_url", baseUrl + "/kakaoPaySuccess");
        params.add("cancel_url", baseUrl + "/kakaoPayCancel");
        params.add("fail_url", baseUrl + "/kakaoPayFail");

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
