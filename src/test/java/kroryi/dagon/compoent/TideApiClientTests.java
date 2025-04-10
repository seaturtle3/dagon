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
    public void testGetTideItems() {

        String obsPostId = "DT_0001";
        String date = "20250410";

        List<TideItemDTO> tideItems = tideApiClient.getTideItems(obsPostId, date);

        log.info("수신된 물때 데이터 수: {}", tideItems.size());

        for (TideItemDTO item : tideItems) {
            log.info("조위 시간: {}, 조위 수위: {}, 조위 코드: {}",
                    item.getTph_time(),
                    item.getTph_level(),
                    item.getHl_code());
        }
    }
}