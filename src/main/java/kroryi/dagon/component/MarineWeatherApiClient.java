package kroryi.dagon.component;

import kroryi.dagon.DTO.multtae.*;
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
@RequiredArgsConstructor
@Log4j2
public class MarineWeatherApiClient {

    private final RestTemplate restTemplate;

    @Value("${marine.api.base-uri}")
    private String baseUri;

    @Value("${marine.api.service-key}")
    private String serviceKey;

    public List<WaveDTO> getWaveData(String obsCode, String date){
        String url = String.format("%s/obsWaveHight/search.do",baseUri);

        URI uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsCode)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .build(false)
                .encode()
                .toUri();

        log.info(" 파고 API 요청: {}", uri);

        MarineBaseResponse<WaveDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<MarineBaseResponse<WaveDTO>>() {}).getBody();

        if (response == null || response.getResult() == null || response.getResult().getData() == null) {
            log.warn("Wave API 응답 없음 또는 result.data가 null - 관측소: {}", obsCode);
            return List.of(); // 비어있는 리스트 반환
        }

        return response.getResult().getData();
    }

    public List<AirTempDTO> getAirTempData(String obsCode, String date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUri + "/tideObsAirTemp/search.do")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsCode)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .build(false)
                .encode()
                .toUri();

        log.info(" 기온 API 요청: {}", uri);
        MarineBaseResponse<AirTempDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<MarineBaseResponse<AirTempDTO>>() {}).getBody();

        return response != null && response.getResult() != null
                ? response.getResult().getData()
                : List.of();
    }

    public List<WindDTO> getWindData(String obsCode, String date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUri + "/tideObsWind/search.do")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsCode)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .build(false)
                .encode()
                .toUri();

        log.info(" 풍속/풍향 API 요청: {}", uri);

        MarineBaseResponse<WindDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<MarineBaseResponse<WindDTO>>() {}).getBody();

        return response != null && response.getResult() != null
                ? response.getResult().getData()
                : List.of();
    }

    public List<TideLevelDTO> getTideLevelData(String obsCode, String date) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUri + "/tideObsPre/search.do")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("ObsCode", obsCode)
                .queryParam("Date", date)
                .queryParam("ResultType", "json")
                .build(false)
                .encode()
                .toUri();

        log.info(" 예측 조위 API 요청: {}", uri);

        MarineBaseResponse<TideLevelDTO> response = restTemplate.exchange(uri, HttpMethod.GET, null,
                new ParameterizedTypeReference<MarineBaseResponse<TideLevelDTO>>() {}).getBody();

        return response != null && response.getResult() != null
                ? response.getResult().getData()
                : List.of();
    }

}
