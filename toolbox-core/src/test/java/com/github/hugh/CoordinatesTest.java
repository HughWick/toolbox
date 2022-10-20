package com.github.hugh;

import com.github.hugh.bean.dto.coordinates.GgaDTO;
import com.github.hugh.bean.dto.coordinates.GpsDTO;
import com.github.hugh.bean.dto.coordinates.RmcDTO;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.CoordinatesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 定位测试了
 * User: AS
 * Date: 2021/4/20 16:55
 */
class CoordinatesTest {

    // 测试高德转百度坐标系
    @Test
    void testGaoDeToBaiDu() {
        GpsDTO gpsDTO = CoordinatesUtils.gcj02ToBd09(112.94671143, 28.21967801);
//        System.out.println("-1-->>" + gpsDTO.getLongitude());
//        System.out.println("-2-->>" + gpsDTO.getLatitude());
        assertEquals(Double.parseDouble("112.95321356"), gpsDTO.getLongitude());
        assertEquals(Double.parseDouble("28.22574022"), gpsDTO.getLatitude());
        GpsDTO gpsDTO2 = CoordinatesUtils.gcj02ToBd09("109.48426107", "28.56126845");
//        System.out.println("-3-->>" + gpsDTO2.getLongitude());
//        System.out.println("-4-->>" + gpsDTO2.getLatitude());
        assertEquals(Double.parseDouble("109.49082055"), gpsDTO2.getLongitude());
        assertEquals(Double.parseDouble("28.56704569"), gpsDTO2.getLatitude());
    }

    // 百度转高德地址
    @Test
    void testBaiDuToGaoDe() {
        // 百度经度
        String bdLongitude = "112.95321356";
        String bdLatitude = "28.22574022";
        GpsDTO gpsDTO = CoordinatesUtils.bd09ToGcj02(bdLongitude, bdLatitude);
        System.out.println("-1-->>" + gpsDTO.getLongitude());
        System.out.println("-2-->>" + gpsDTO.getLatitude());
        assertEquals(Double.parseDouble("112.94671139"), gpsDTO.getLongitude());
        assertEquals(Double.parseDouble("28.21967804"), gpsDTO.getLatitude());
    }

    // 测试GGA
    @Test
    void testGga() {
        String lons = "2832.9191";
        String lats = "10922.5659";
        String lon = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(lons));
        String lat = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(lats));
        System.out.println(lon + "-----------" + lat);
        String str = "$GNGGA,063012.000,2832.9110,N,10922.5671,E,2,21,0.63,400.9,M,-27.1,M,,*5A";
        GgaDTO ggaDTO = CoordinatesUtils.parseGga(str);
        System.out.println(ggaDTO);
        assertTrue(ggaDTO != null);
        assertEquals("063012.000", ggaDTO.getDate());
        assertEquals("2832.9110", ggaDTO.getLatitude());
        assertEquals("10922.5671", ggaDTO.getLongitude());
//        String str2 = "$GNGGA,033652.000,2517.4725,N,11040.8766,E,1,3,37.16,-323.4,M,-19.8,M,,*7B";
//        GgaDTO ggaDTO2 = CoordinatesUtils.parseGga(str2);
//        System.out.println(ggaDTO2);
//        String lon2 = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(ggaDTO2.getLongitude()));
//        String lat2 = CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(ggaDTO2.getLatitude()));
//        System.out.println(lon2 + "-----------" + lat2);
    }

    @Test
    void testRmc() {
        String rmc = "$GNRMC,063234.00,A,2813.3615,N,11256.4649,E,0.329,,201022,,,A,V*18";
        RmcDTO rmcDTO = CoordinatesUtils.parseRmc(rmc);
        System.out.println(rmcDTO);
        assertEquals("2022-10-20 14:32:34", rmcDTO.getReadingDate());
        assertEquals("A", rmcDTO.getStatus());
        assertEquals("11256.4649", rmcDTO.getLongitude());
        assertEquals("2813.3615", rmcDTO.getLatitude());
        // 空
        Assertions.assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.parseRmc(""));
    }
}
