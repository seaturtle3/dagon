package kroryi.dagon.service.multtae;

import kroryi.dagon.component.LunarApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LunarCacheService {

    private final LunarApiClient lunarApiClient;
    private final Map<LocalDate, Double> lunarCache = new ConcurrentHashMap<>();

    public Double getLunarAge(LocalDate date) {
        return lunarCache.computeIfAbsent(date, d -> {
            Double result = lunarApiClient.getLunarAge(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
            return result != null ? result : -1.0; // null 방지
        });
    }
}