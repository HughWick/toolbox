package com.github.hugh;

import com.github.hugh.model.dto.GpsDTO;
import com.github.hugh.util.CoordinatesUtils;
import org.junit.Test;

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
        GpsDTO gpsDTO =CoordinatesUtils.bd09ToGcj02(112.95321356, 28.22574022);
//        double[] doubles2 = CoordinatesUtils.gcj02ToBd09(0, 0);
//        System.out.println(ArrayUtils.toString(doubles, ","));
        System.out.println("-1-->>" + gpsDTO.getLongitude());
        System.out.println("-2-->>" + gpsDTO.getLatitude());
    }

}
