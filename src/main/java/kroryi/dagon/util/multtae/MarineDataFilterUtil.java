package kroryi.dagon.util.multtae;

import kroryi.dagon.DTO.multtae.HasRecordTime;

import java.util.List;

public class MarineDataFilterUtil {
    private static final List<Integer> TARGET_HOURS = List.of(0, 3, 6, 9, 12, 15, 18, 21);

    public static <T extends HasRecordTime> List<T> filterByHour(List<T> list) {
        if (list == null) return List.of();

        return list.stream()
                .filter(dto -> {
                    try {
                        String time = dto.getRecord_time().substring(11, 16);
                        int hour = Integer.parseInt(time.substring(0, 2));
                        return TARGET_HOURS.contains(hour);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();
    }
}