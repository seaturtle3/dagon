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
        List<HourlyDataDTO> result = new ArrayList<>();

        for (Integer hour : TARGET_HOURS) {
            LocalTime targetTime = LocalTime.of(hour, 0);

            WaveDTO wave = findClosest(waveList, targetTime);
            WindDTO wind = findClosest(windList, targetTime);
            AirTempDTO air = findClosest(airTempList, targetTime);
            TideLevelDTO tide = findClosest(tideLevelList, targetTime);

            HourlyDataDTO dto = HourlyDataDTO.builder()
                    .time(hour + "시")
                    .wave(wave != null ? wave.getWave() : null)
                    .wind_speed(wind != null ? wind.getWind_speed() : null)
                    .wind_dir(wind != null ? wind.getWind_dir() : null)
                    .air_temp(air != null ? air.getAir_temp() : null)
                    .tide_level(tide != null ? tide.getTide_level() : null)
                    .build();

            result.add(dto);
        }

        return result;
    }

    private static <T extends HasRecordTime> T findClosest(List<T> list, LocalTime target) {
        if (list == null || list.isEmpty()) return null;

        T closest = null;
        long minDiff = Long.MAX_VALUE;

        for (T item : list) {
            try {
                String recordTime = item.getRecord_time();

                String timeStr = recordTime.substring(11, 19);
                LocalTime itemTime = LocalTime.parse(timeStr); // LocalTime으로 변환
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