package kroryi.dagon.DTO.multtae;

import lombok.extern.log4j.Log4j2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
public class MarineDataMapper {
    private static final List<Integer> TARGET_HOURS = List.of(0, 3, 6, 9, 12, 15, 18, 21);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<HourlyDataDTO> mergeToHourly(
            List<WaveDTO> waveList,
            List<WindDTO> windList,
            List<AirTempDTO> airTempList,
            List<TideLevelDTO> tideLevelList
    ) {
        return TARGET_HOURS.stream()
                .map(hour -> {
                    LocalTime targetTime = LocalTime.of(hour, 0);
                    return HourlyDataDTO.builder()
                            .time(hour + "시")
                            .wave(getValue(waveList, targetTime, WaveDTO::getWave))
                            .wind_speed(getValue(windList, targetTime, WindDTO::getWind_speed))
                            .wind_dir(getValue(windList, targetTime, WindDTO::getWind_dir))
                            .air_temp(getValue(airTempList, targetTime, AirTempDTO::getAir_temp))
                            .tide_level(getValue(tideLevelList, targetTime, TideLevelDTO::getTide_level))
                            .build();
                })
                .toList();
    }

    private static <T extends HasRecordTime, R> R getValue(List<T> list, LocalTime target, java.util.function.Function<T, R> extractor) {
        T item = findClosest(list, target);
        return item != null ? extractor.apply(item) : null;
    }

    public static <T extends HasRecordTime> T findClosest(List<T> list, LocalTime target) {
        if (list == null || list.isEmpty()) return null;

        T closest = null;
        long minDiff = Long.MAX_VALUE;

        for (T item : list) {
            try {
                String recordTime = item.getRecord_time();

                // "HH:mm" 또는 "HH:mm:ss" 추출
                String timeStr = recordTime.substring(11).trim(); // 11부터 끝까지 자름
                if (timeStr.length() == 5) timeStr += ":00";      // "HH:mm" → "HH:mm:00"

                LocalTime itemTime = LocalTime.parse(timeStr);    // LocalTime으로 변환
                long diff = Math.abs(itemTime.toSecondOfDay() - target.toSecondOfDay());

                if (diff < minDiff) {
                    minDiff = diff;
                    closest = item;
                }
            } catch (Exception e) {
                log.warn("시간 파싱 실패: {}", item.getRecord_time());
            }
        }
        return closest;
    }
}