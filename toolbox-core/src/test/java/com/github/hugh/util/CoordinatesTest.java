package com.github.hugh.util;

import com.github.hugh.bean.dto.coordinates.GgaDTO;
import com.github.hugh.bean.dto.coordinates.GpsDTO;
import com.github.hugh.bean.dto.coordinates.RmcDTO;
import com.github.hugh.exception.ToolboxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 定位测试了
 * User: AS
 * Date: 2021/4/20 16:55
 */
class CoordinatesTest {

    // 测试高德转百度坐标系
    @Test
    void testGaoDeToBaiDu() {
        String long1 = "112.95321356";
        String latitude2 = "28.22574022";
        // 112.95321356,28.22574022
        GpsDTO gpsDTO = CoordinatesUtils.gcj02ToBd09(112.94671143, 28.21967801);
        assertEquals(Double.parseDouble(long1), gpsDTO.getLongitude());
        assertEquals(Double.parseDouble(latitude2), gpsDTO.getLatitude());
        GpsDTO gpsDTO2 = CoordinatesUtils.gcj02ToBd09("109.48426107", "28.56126845");
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
//        System.out.println("-1-->>" + gpsDTO.getLongitude());
//        System.out.println("-2-->>" + gpsDTO.getLatitude());
        assertEquals(Double.parseDouble("112.94671139"), gpsDTO.getLongitude());
        assertEquals(Double.parseDouble("28.21967804"), gpsDTO.getLatitude());
    }

    // 测试GGA
    @Test
    void testGga() {
        String lons = "2832.9191";
        String lats = "10922.5659";
        assertEquals("109.37609833", CoordinatesUtils.formatDegreeMinutes(Double.parseDouble(lats)));
        assertEquals("28.54865167", CoordinatesUtils.formatDegreeMinutes(lons));
        String str = "$GNGGA,063012.000,2832.9110,N,10922.5671,E,2,21,0.63,400.9,M,-27.1,M,,*5A";
        GgaDTO ggaDTO = CoordinatesUtils.parseGga(str);
        assertNotNull(ggaDTO);
        assertEquals("063012.000", ggaDTO.getDate());
        assertEquals("N", ggaDTO.getLatitudeBearing());
        assertEquals("E", ggaDTO.getLongitudeBearing());
        assertEquals("2", ggaDTO.getGpsStatus());
        assertEquals("0.63", ggaDTO.getHdopHorizontalAccuracyFactor());
        assertEquals("400.9", ggaDTO.getAltitude());
        assertEquals("M", ggaDTO.getWaterSurfaceAltitude());
        assertEquals("-27.1", ggaDTO.getDifferentialTime());
        assertEquals("M", ggaDTO.getDifferentialStationId());
        assertEquals("*5A", ggaDTO.getCalibrationValue());
        assertEquals("2832.9110", ggaDTO.getLatitude());
        assertEquals("10922.5671", ggaDTO.getLongitude());
        assertEquals("21", ggaDTO.getNumberOfSatellites());
        assertEquals("14:30:12", ggaDTO.getReadingDate());
        String str2 = "GNGGA,024258.00,2813.3850,N,11256.4860,E,1,11,0.85,61.5,M,,M,,*67";
        GgaDTO ggaDTO2 = CoordinatesUtils.parseGga(str2);
        assertEquals("10:42:58", ggaDTO2.getReadingDate());
        String str3 = "GNGGA,021258.0,2813.3850,N,11256.4860,E,1,11,0.85,61.5,M,,M,,*67";
        GgaDTO ggaDTO3 = CoordinatesUtils.parseGga(str3);
        assertEquals("10:12:58", ggaDTO3.getReadingDate());
        String str4 = "$GNGGA,033652,2517.4725,N,11040.8766,E,1,3,37.16,-323.4,M,-19.8,M,,*7B";
        GgaDTO ggaDTO4 = CoordinatesUtils.parseGga(str4);
        assertEquals("11:36:52", ggaDTO4.getReadingDate());
        // 定位失败，且卫星数量为0
        String str5 = "$GNGGA,064603.00,2813.1906,N,11241.3959,E,0,00,99.99,-69.7,M,,M,,*78";
        GgaDTO ggaDTO5 = CoordinatesUtils.parseGga(str5);
        assertEquals("00", ggaDTO5.getNumberOfSatellites());
        assertEquals("0", ggaDTO5.getGpsStatus());
//        assertEquals("11:36:52", ggaDTO4.getReadingDate());

    }

    @Test
    void testRmc() {
        String rmc = "$GNRMC,063234.00,A,2813.3615,N,11256.4649,E,0.329,,201022,,,A,V*18";
        RmcDTO rmcDTO = CoordinatesUtils.parseRmc(rmc);
//        System.out.println(rmcDTO);
        assertEquals("2022-10-20 14:32:34", rmcDTO.getReadingDate());
        assertEquals("A", rmcDTO.getStatus());
        assertEquals("11256.4649", rmcDTO.getLongitude());
        assertEquals("2813.3615", rmcDTO.getLatitude());
        // 空
        assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.parseRmc(""));

        String rmc2 = "$GNRMC,024926.00,A,2813.3480,N,11256.4691,E,0.237,,050523,,,A,V*14\\r\\n\\r\\nOK\\r\\n";
        RmcDTO rmcDTO2 = CoordinatesUtils.parseRmc(rmc2);
        assertEquals("2023-05-05 10:49:26", rmcDTO2.getReadingDate());
        assertEquals("A", rmcDTO2.getStatus());
//        assertEquals("V*14", rmcDTO2.getCalibrationValue());


        String rmc3 = "$GNRMC,004046.00,A,2714.85989,N,11128.20777,E,0.301,,080125,,,A,V*15\\r\\n\\r\\nOK\\r\\n";
        RmcDTO rmcDTO3 = CoordinatesUtils.parseRmc(rmc3);
        System.out.println(rmcDTO3);
    }


    // 测试两点间距离
    @Test
    void testDistance() {
        String longitude1 = "116.368904";
        String latitude1 = "39.923423";
        String longitude2 = "116.387271";
        String latitude2 = "39.922501";
        final double distance = CoordinatesUtils.getDistance(Double.parseDouble(longitude1), Double.parseDouble(latitude1), Double.parseDouble(longitude2), Double.parseDouble(latitude2));
        assertEquals(1571.38, distance);
        // 112.944468,28.218373
        String longitude3 = "112.944468";
        String latitude3 = "28.218373";
        // 112.933732,28.280851
        String longitude4 = "112.933732";
        String latitude4 = "28.280851";
        final double distance2 = CoordinatesUtils.getDistance(longitude3, latitude3, longitude4, latitude4);
        assertEquals(7034.25, distance2);
        String lngAndLat1 = "112.944468,28.218373";
        String lngAndLat2 = "112.933732,28.280851";
        final double distance3 = CoordinatesUtils.getDistance(lngAndLat1, lngAndLat2);
        assertEquals(distance3, distance2);
    }

    // 测试错误的经纬度验证
    @Test
    void testErrorDistance() {
        // 正确数值
        String lngAndLat1 = "112.944468,28.218373";
        String lngAndLat2 = "112.933732,28.280851";
        //=========================================
        String lngAndLat3 = "112.944468,28.218373";
        String lngAndLat4 = "112.933732，28.280851";
        final ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat3, lngAndLat4));
        assertEquals("latitude and longitude separator does not exist", toolboxException.getMessage());
        String lngAndLat5 = "112.944468，28.218373";
        String lngAndLat6 = "112.933732,28.280851";
        final ToolboxException toolboxException2 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat5, lngAndLat6));
        assertEquals("latitude and longitude separator does not exist", toolboxException2.getMessage());
        String lngAndLat7 = "112.944468,28.218373,";
        String lngAndLat8 = "112.933732,28.280851";
        final ToolboxException toolboxException3 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat7, lngAndLat8));
        assertEquals("multiple latitude and longitude separators", toolboxException3.getMessage());
        String lngAndLat9 = "112.944468,28.218373";
        String lngAndLat10 = "112.933732,28.280851,";
        final ToolboxException toolboxException4 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat9, lngAndLat10));
        assertEquals("multiple latitude and longitude separators", toolboxException4.getMessage());
        String lngAndLat11 = "1128.218373,112.944468";
        String lngAndLat12 = "112.933732,28.280851";
        final ToolboxException toolboxException5 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat11, lngAndLat12));
        assertEquals("first longitude error : " + lngAndLat11.split(",")[0], toolboxException5.getMessage());
        String lngAndLat13 = "112.218373,112.944468";
        String lngAndLat14 = "1112.933732,28.280851";
        final ToolboxException toolboxException6 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat13, lngAndLat14));
        assertEquals("second longitude error : " + lngAndLat14.split(",")[0], toolboxException6.getMessage());
        String lngAndLat15 = "112.218373,112.944468";
        final ToolboxException toolboxException7 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat15, lngAndLat1));
        assertEquals("first latitude error : " + lngAndLat15.split(",")[1], toolboxException7.getMessage());
        String lngAndLat16 = "112.218373,112.944468";
        final ToolboxException toolboxException8 = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.getDistance(lngAndLat1, lngAndLat16));
        assertEquals("second latitude error : " + lngAndLat16.split(",")[1], toolboxException8.getMessage());

    }


    /**
     * GGA时空的时候
     */
    @Test
    void testEmpty() {
        String str1 = "$GNGGA,015830.00,,,,,0,00,99.99,,,,,,*77\\r\\n\\r\\nOK\\r\\n";
        GgaDTO ggaDTO = CoordinatesUtils.parseGga(str1);
//        System.out.println("====>>" + ggaDTO);
        assertTrue(EmptyUtils.isEmpty(ggaDTO.getLongitude()));

    }


    @Test
    void testReal(){
        String str1 = "$GNGGA,203643.00,2712.02552,N,11127.12077,E\\r\\n\\r\\nOK\\r\\n";
//        GgaDTO ggaDTO = CoordinatesUtils.parseGga(str1);
        ToolboxException toolboxException = assertThrowsExactly(ToolboxException.class, () -> CoordinatesUtils.parseGga(str1));
        assertEquals("Invalid GGA string, insufficient data.", toolboxException.getMessage());
    }

}
