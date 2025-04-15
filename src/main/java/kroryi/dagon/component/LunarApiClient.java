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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Log4j2
public class LunarApiClient {

    @Value("${lunar.api.base-url}")
    private String baseUrl;

    @Value("${lunar.api.service-key}")
    private String serviceKey;

    public Double getLunarAge(int year, int month, int day) {

        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/getLunPhInfo")
                    .queryParam("ServiceKey", encodedKey)
                    .queryParam("solYear", year)
                    .queryParam("solMonth", String.format("%02d", month))
                    .queryParam("solDay", String.format("%02d", day))
                    .build(true)
                    .toUri();

            log.info("요청된 월령 API URI: {}", uri);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri.toString());

            XPath xPath = XPathFactory.newInstance().newXPath();
            String lunAgeStr = xPath.compile("/response/body/items/item/lunAge")
                    .evaluate(doc, XPathConstants.STRING)
                    .toString();

            if (!lunAgeStr.isBlank()) {
                log.info("파싱된 lunAge 값: {}", lunAgeStr);
                return Double.parseDouble(lunAgeStr);
            } else {
                log.warn("XPath로 lunAge 값이 없음: {}", uri);
                return null;
            }

        } catch (Exception e) {
            log.error("월령 API 요청 중 오류", e);
            return null;
        }

    }


}
