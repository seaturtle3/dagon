package kroryi.dagon.service.multtae;

import kroryi.dagon.component.SunriseApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SunriseCacheService {

    private final SunriseApiClient sunriseApiClient;

    // key: "부산:2025-04-21"
    private final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    public Map<String, String> getSunInfo(String location, LocalDate date) {
        String key = location + ":" + date.toString();
        return cache.computeIfAbsent(key, k -> sunriseApiClient.getSunriseSunset(location, date));
    }
}