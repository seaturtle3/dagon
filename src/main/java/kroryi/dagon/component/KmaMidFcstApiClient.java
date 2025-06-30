package kroryi.dagon.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Log4j2
@RequiredArgsConstructor
public class KmaMidFcstApiClient {

    @Value("${kma.midfcst.api.base-url}")
    private String baseUrl;

    @Value("${kma.midfcst.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    /**
     * 중기예보(육상) API 호출 (공식문서 기준)
     */
    public String getMidFcst(String stnId, String tmFc) {
        String url = baseUrl + "/getMidFcst";
        String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", encodedKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "10")
                .queryParam("dataType", "JSON")
                .queryParam("stnId", stnId)
                .queryParam("tmFc", tmFc);
        log.info("기상청 중기예보 API 요청: {}", builder.toUriString());
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }
} 