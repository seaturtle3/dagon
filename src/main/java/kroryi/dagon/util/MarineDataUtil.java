package kroryi.dagon.util;


import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class MarineDataUtil {

    public static final List<Integer> DEFAULT_HOURS = List.of(0, 3, 6, 9, 12, 15, 18, 21);

    public static <T extends HasRecordTime> List<T> filterByTargetHours(List<T> data, List<Integer> hours) {
        if (data == null) return List.of(); // null 체크 추가
        return data.stream()
                .filter(d -> {
                    try {
                        String timeStr = d.getRecord_time().substring(11, 16); // "HH:mm"
                        LocalTime localTime = LocalTime.parse(timeStr);

                        int hour = localTime.getHour();
                        int minute = localTime.getMinute();
                        return hours.contains(hour) && minute == 0;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
