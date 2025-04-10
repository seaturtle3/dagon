package kroryi.dagon.compoent;

import com.fasterxml.jackson.databind.ObjectMapper;
import kroryi.dagon.DTO.TideInfoDTO;
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


}
