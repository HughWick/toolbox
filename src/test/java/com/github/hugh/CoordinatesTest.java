package com.github.hugh;

import com.github.hugh.model.dto.GgaDTO;
import com.github.hugh.model.dto.GpsDTO;
import com.github.hugh.util.CoordinatesUtils;
import org.junit.jupiter.api.Test;

/**
 * User: AS
 * Date: 2021/4/20 16:55
 */
public class CoordinatesTest {

    @Test
    public void test01() {
        GpsDTO gpsDTO = CoordinatesUtils.gcj02ToBd09(112.94671143, 28.21967801);
//        double[] doubles2 = CoordinatesUtils.gcj02ToBd09(0, 0);
//        System.out.println(ArrayUtils.toString(doubles, ","));
        System.out.println("-1-->>" + gpsDTO.getLongitude());
        System.out.println("-2-->>" + gpsDTO.getLatitude());
    }

    @Test
    public void test02() {
        GpsDTO gpsDTO = CoordinatesUtils.bd09ToGcj02(112.95321356, 28.22574022);
//        double[] doubles2 = CoordinatesUtils.gcj02ToBd09(0, 0);
//        System.out.println(ArrayUtils.toString(doubles, ","));
        System.out.println("-1-->>" + gpsDTO.getLongitude());
        System.out.println("-2-->>" + gpsDTO.getLatitude());
    }

    @Test
    public void test03() {
        String lons = "2832.9191";
        String lats = "10922.5659";
        String lon = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(lons));
        String lat = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(lats));
        System.out.println(lon + "-----------" + lat);
        String str = "$GNGGA,063012.000,2832.9110,N,10922.5671,E,2,21,0.63,400.9,M,-27.1,M,,*5A";
        GgaDTO ggaDTO = CoordinatesUtils.parseGga(str);
        System.out.println(ggaDTO);
    }
}
