package kroryi.dagon.component;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class SunriseApiClient {

    @Value("${sun.api.base-url}")
    private String baseUrl;

    @Value("${sun.api.service-key}")
    private String serviceKey;
    public Map<String, String> getSunriseSunset(String location, LocalDate date) {
        try {
            String locdate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String endcodeLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/getAreaRiseSetInfo")
                    .queryParam("ServiceKey", encodedKey)
                    .queryParam("locdate", locdate)
                    .queryParam("location", endcodeLocation)
                    .build(true)
                    .toUri();

            log.info("일출일몰 API 요청 URI: {}", uri);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri.toString());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String sunrise = xPath.compile("/response/body/items/item/sunrise").evaluate(doc);
            String sunset = xPath.compile("/response/body/items/item/sunset").evaluate(doc);


            log.info("일출: {}, 일몰: {}", sunrise, sunset);

            return Map.of(
                    "sunrise", sunrise,
                    "sunset", sunset
            );

        } catch (Exception e) {
            log.error("일출일몰 API 연동 실패", e);
            return Map.of(
                    "sunrise", "-",
                    "sunset", "-"
            );
        }

    }

}
