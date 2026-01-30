package com.github.hugh.bean.dto.coordinates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GgaDTO 测试类
 */
class GgaDTOTest {

    @Test
    @DisplayName("全字段赋值与读取测试 - 确保数据链路正常")
    void testGgaDTOFullFields() {
        GgaDTO gga = new GgaDTO();

        // 模拟标准的 NMEA $GPGGA 数据赋值
        gga.setName("$GPGGA");
        gga.setDate("092204.00");
        gga.setLatitude("3954.5641");
        gga.setLatitudeBearing("N");
        gga.setLongitude("11623.2341");
        gga.setLongitudeBearing("E");
        gga.setGpsStatus("1");
        gga.setNumberOfSatellites("08");
        gga.setHdopHorizontalAccuracyFactor("1.1");
        gga.setAltitude("45.4");
        gga.setWaterSurfaceAltitude("0.0");
        gga.setDifferentialTime("null");
        gga.setDifferentialStationId("null");
        gga.setCalibrationValue("*6A");
        gga.setReadingDate("2026-01-29 16:42:01");

        // 断言逻辑
        assertAll("GgaDTO 字段校验",
                () -> assertEquals("$GPGGA", gga.getName()),
                () -> assertEquals("092204.00", gga.getDate()),
                () -> assertEquals("3954.5641", gga.getLatitude()),
                () -> assertEquals("N", gga.getLatitudeBearing()),
                () -> assertEquals("11623.2341", gga.getLongitude()),
                () -> assertEquals("E", gga.getLongitudeBearing()),
                () -> assertEquals("1", gga.getGpsStatus()),
                () -> assertEquals("08", gga.getNumberOfSatellites()),
                () -> assertEquals("1.1", gga.getHdopHorizontalAccuracyFactor()),
                () -> assertEquals("45.4", gga.getAltitude()),
                () -> assertEquals("*6A", gga.getCalibrationValue())
        );
    }

    @Test
    @DisplayName("Lombok Equals/HashCode 校验 - 确保对象比较逻辑正确")
    void testEqualsAndHashCode() {
        GgaDTO gga1 = new GgaDTO();
        gga1.setName("$GPGGA");
        gga1.setDate("123456");

        GgaDTO gga2 = new GgaDTO();
        gga2.setName("$GPGGA");
        gga2.setDate("123456");

        GgaDTO gga3 = new GgaDTO();
        gga3.setName("$GPGGA");
        gga3.setDate("000000");

        assertAll("对象一致性校验",
                () -> assertEquals(gga1, gga2, "相同数据的对象应该相等"),
                () -> assertNotEquals(gga1, gga3, "不同数据的对象不应相等"),
                () -> assertEquals(gga1.hashCode(), gga2.hashCode(), "相等对象的 HashCode 必须一致")
        );
    }

    @Test
    @DisplayName("ToString 格式校验 - 方便日志排查")
    void testToString() {
        GgaDTO gga = new GgaDTO();
        gga.setName("$GPGGA");

        String toStringResult = gga.toString();

        assertTrue(toStringResult.contains("name=$GPGGA"), "toString 应该包含类属性名和值");
        assertTrue(toStringResult.contains("GgaDTO"), "toString 应该包含类名");
    }
}
