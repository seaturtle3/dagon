package kroryi.dagon.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Log4j2
@RequiredArgsConstructor
public class KmaVilageFcstApiClient {

    @Value("${kma.vilagefcst.api.base-url}")
    private String baseUrl;

    @Value("${kma.vilagefcst.api.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    /**
     * 단기예보(동네예보) API 호출
     */
    public String getVilageFcst(int nx, int ny, String baseDate, String baseTime) {
        String url = baseUrl + "/getVilageFcst";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", "1000")
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny);
        log.info("기상청 단기예보 API 요청: {}", builder.toUriString());
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    /**
     * 위경도 → 격자(nx, ny) 변환 (기상청 공식)
     * @param lat 위도
     * @param lon 경도
     * @return int[]{nx, ny}
     */
    public static int[] convertToGrid(double lat, double lon) {
        // 기상청 표준격자 변환 공식 (Lambert Conformal Conic)
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0;      // 격자 간격(km)
        double SLAT1 = 30.0;    // 투영 위도1(degree)
        double SLAT2 = 60.0;    // 투영 위도2(degree)
        double OLON = 126.0;    // 기준점 경도(degree)
        double OLAT = 38.0;     // 기준점 위도(degree)
        double XO = 43;         // 기준점 X좌표(GRID)
        double YO = 136;        // 기준점 Y좌표(GRID)

        double DEGRAD = Math.PI / 180.0;
        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        double ra = Math.tan(Math.PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lon * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        int x = (int) Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int y = (int) Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        return new int[]{x, y};
    }
} 