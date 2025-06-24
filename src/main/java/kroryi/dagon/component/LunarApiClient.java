package kroryi.dagon.component;

import kroryi.dagon.DTO.multtae.SunRiseSetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@RequiredArgsConstructor
public class LunarApiClient {

    @Value("${lunar.api.base-url}")
    private String lunarBaseUrl;

    @Value("${sun.api.base-url}")
    private String sunBaseUrl;

    @Value("${lunar.api.service-key}")
    private String serviceKey;

    public Double getLunarAge(int year, int month, int day) {

        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(lunarBaseUrl + "/getLunPhInfo")
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

    /**
     * @deprecated getAreaRiseSet(LocalDate, String) 사용 권장
     */
    @Deprecated
    public SunRiseSetDTO getSunRiseSet(LocalDate date, double longitude, double latitude) {
        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(sunBaseUrl + "/getSunRiseSetInfo")
                    .queryParam("serviceKey", encodedKey)
                    .queryParam("locdate", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .queryParam("longitude", longitude)
                    .queryParam("latitude", latitude)
                    .queryParam("dnYn", "y") //  일출/일몰 사이 시간(낮) 계산 여부
                    .build(true)
                    .toUri();

            log.info("일출/일몰 API 요청 URI: {}", uri);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri.toString());

            // --- 디버깅용 로그 추가 ---
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            log.info("🌞 천문연구원 API 응답 (Raw XML): {}", writer.toString());
            // --- 디버깅용 로그 끝 ---

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList itemList = (NodeList) xPath.compile("/response/body/items/item").evaluate(doc, XPathConstants.NODESET);

            if (itemList.getLength() > 0) {
                Element item = (Element) itemList.item(0);
                String sunriseStr = item.getElementsByTagName("sunrise").item(0).getTextContent().trim();
                String sunsetStr = item.getElementsByTagName("sunset").item(0).getTextContent().trim();

                log.info("🌞 파싱 결과 - 일출: [{}], 일몰: [{}]", sunriseStr, sunsetStr);

                return SunRiseSetDTO.builder()
                        .sunrise(formatTime(sunriseStr))
                        .sunset(formatTime(sunsetStr))
                        .build();
            } else {
                log.warn("일출/일몰 정보를 찾을 수 없습니다. URI: {}", uri);
                return null;
            }

        } catch (Exception e) {
            log.error("일출/일몰 API 요청 중 오류 발생", e);
            return null;
        }
    }

    public SunRiseSetDTO getAreaRiseSet(LocalDate date, String location) {
        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

            URI uri = UriComponentsBuilder.fromHttpUrl(sunBaseUrl + "/getAreaRiseSetInfo")
                    .queryParam("serviceKey", encodedKey)
                    .queryParam("locdate", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .queryParam("location", encodedLocation)
                    .build(true)
                    .toUri();

            log.info("일출/일몰 API 요청 (지역기반) URI: {}", uri);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri.toString());

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            log.info("🌞 천문연구원 API 응답 (Raw XML, 지역기반): {}", writer.toString());

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList itemList = (NodeList) xPath.compile("/response/body/items/item").evaluate(doc, XPathConstants.NODESET);

            if (itemList.getLength() > 0) {
                Element item = (Element) itemList.item(0);
                String sunriseStr = item.getElementsByTagName("sunrise").item(0).getTextContent().trim();
                String sunsetStr = item.getElementsByTagName("sunset").item(0).getTextContent().trim();

                log.info("🌞 파싱 결과 (지역기반) - 일출: [{}], 일몰: [{}]", sunriseStr, sunsetStr);

                return SunRiseSetDTO.builder()
                        .sunrise(formatTime(sunriseStr))
                        .sunset(formatTime(sunsetStr))
                        .build();
            } else {
                log.warn("일출/일몰 정보를 찾을 수 없습니다. URI: {}", uri);
                return null;
            }

        } catch (Exception e) {
            log.error("일출/일몰 API 요청(지역기반) 중 오류 발생", e);
            return null;
        }
    }

    private String formatTime(String timeStr) {
        if (timeStr == null || timeStr.length() != 4) return "정보 없음";
        return timeStr.substring(0, 2) + ":" + timeStr.substring(2, 4);
    }

}
