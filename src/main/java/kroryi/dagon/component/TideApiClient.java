package kroryi.dagon.component;

import kroryi.dagon.DTO.TideItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TideApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${tide.api.base-url}")
    private String baseUrl;

    @Value("${tide.api.service-key}")
    private String serviceKey;

    public List<TideItemDTO> getTideItems(String obsCode, String date){

        String url = UriComponentsBuilder
                .fromUri(URI.create(baseUrl))
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsCode)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .toUriString();

        System.out.println("요청 URL: " + url);

        // key테스트 중
        String rawResponse = restTemplate.getForObject(url, String.class);
        System.out.println("응답 본문 (text): " + rawResponse);

//        TideInfoDTO response = restTemplate.getForObject(url, TideInfoDTO.class);
//        return response.getResult().getData();

        return List.of();
    }

}
