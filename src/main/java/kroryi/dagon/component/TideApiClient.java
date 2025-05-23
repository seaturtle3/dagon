package kroryi.dagon.component;

import kroryi.dagon.DTO.multtae.MarineBaseResponse;
import kroryi.dagon.DTO.multtae.TideItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class TideApiClient {

    private final RestTemplate restTemplate;

    @Value("${tide.api.base-url}")
    private String baseUrl;

    @Value("${tide.api.service-key}")
    private String serviceKey;

    public List<TideItemDTO> getTideItems(String obsPostId, String date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsPostId)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .build(false)
                .encode()
                .toUri();

        log.info("물때 API 요청 URI: {}", uri);

        MarineBaseResponse<TideItemDTO> response =
                restTemplate.exchange(uri, HttpMethod.GET, null,
                        new ParameterizedTypeReference<MarineBaseResponse<TideItemDTO>>() {}).getBody();

        if (response != null && response.getResult() != null) {
            return response.getResult().getData();
        } else {
            log.warn("API 응답이 null이거나 형식이 맞지 않음");
            return List.of();
        }
    }
}