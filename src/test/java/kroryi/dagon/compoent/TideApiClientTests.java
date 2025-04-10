package kroryi.dagon.compoent;


import kroryi.dagon.DTO.TideItemDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class TideApiClientTests {
    @Autowired
    private TideApiClient tideApiClient;

    @Test
    public void testGetTideItems() { // 키 인식안되는 중
        String obsCode = "DT_0001";
        String date = "20250410";   // 테스트용 날짜

        List<TideItemDTO> result = tideApiClient.getTideItems(obsCode, date);

        result.forEach(dto -> {
            log.info("시간: {}", dto.getTph_time());
            log.info("수위: {}", dto.getTph_level());
            log.info("상태: {}", dto.getH1_code());
        });
    }

}
