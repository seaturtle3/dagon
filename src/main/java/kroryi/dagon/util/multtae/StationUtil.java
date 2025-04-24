package kroryi.dagon.util.multtae;

import kroryi.dagon.entity.multtae.TideStation;
import kroryi.dagon.entity.multtae.WaveStation;

import java.util.List;

public class StationUtil {

    public static WaveStation findClosestWaveStation(TideStation tideStation, List<WaveStation> waveStations) {
        double lat1 = tideStation.getLatitude();
        double lon1 = tideStation.getLongitude();

        WaveStation closest = null;
        double minDistance = Double.MAX_VALUE;

        for (WaveStation wave : waveStations) {
            double distance = haversine(lat1, lon1, wave.getLatitude(), wave.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                closest = wave;
            }
        }

        return closest;
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}