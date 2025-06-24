package kroryi.dagon.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Log4j2
public class KmaMidFcstApiClient {

    @Value("${kma.midfcst.api.base-url}")
    private String baseUrl;

    @Value("${kma.midfcst.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 중기예보(육상) API 호출
     */
    public String getMidLandFcst(String regId, String tmFc) {
        String url = baseUrl + "/getMidLandFcst";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "10")
                .queryParam("dataType", "JSON")
                .queryParam("regId", regId)
                .queryParam("tmFc", tmFc);
        log.info("기상청 중기예보 API 요청: {}", builder.toUriString());
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }
} 