package kroryi.dagon.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URI;

@Component
@RequiredArgsConstructor
@Log4j2
public class LunarApiClient {
    private final RestTemplate restTemplate;

    @Value("${lunar.api.base-url}")
    private String baseUrl;

    @Value("${lunar.api.service-key}")
    private String serviceKey;


    public Double getLunarAge(int year, int month, int day) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/getLunPhInfo")
                .queryParam("ServiceKey", serviceKey)
                .queryParam("solYear", year)
                .queryParam("solMonth", String.format("%02d", month))
                .queryParam("solDay", String.format("%02d", day))
                .build(false)
                .encode()
                .toUri();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri.toString());

            NodeList lunAgeList = doc.getElementsByTagName("lunAge");
            if (lunAgeList.getLength() > 0) {
                String lunAgeStr = lunAgeList.item(0).getTextContent();
                return Double.parseDouble(lunAgeStr);
            } else {
                log.warn("월령 정보가 없음: {}", uri);
                return null;
            }

        } catch (Exception e) {
            log.error("월령 API 요청 중 오류", e);
            return null;
        }
    }
}
