package com.github.hugh.bean.dto.coordinates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RmcDTO 单元测试
 * 覆盖全字段赋值、Lombok 自动生成的方法以及 NMEA 协议模拟数据
 */
class RmcDTOTest {

    @Test
    @DisplayName("验证 RmcDTO 全字段 Setter 和 Getter 的准确性")
    void testRmcDTOFields() {
        RmcDTO rmc = new RmcDTO();

        // 模拟一条标准的 $GPRMC 数据:
        // $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
        rmc.setName("$GPRMC");
        rmc.setTime("123519.000");
        rmc.setStatus("A");
        rmc.setLatitude("4807.038");
        rmc.setLatitudeBearing("N");
        rmc.setLongitude("01131.000");
        rmc.setLongitudeBearing("E");
        rmc.setSpeed("022.4");
        rmc.setAzimuth("084.4");
        rmc.setDate("230394");
        rmc.setMagneticDeclination("003.1");
        rmc.setDirectionOfMagneticDeclination("W");
        rmc.setMode("A");
        rmc.setCalibrationValue("*6A");
        rmc.setReadingDate("2026-01-29 16:50:00");

        // 使用 assertAll 一次性校验所有字段，避免单点断言失败导致后续无法测试
        assertAll("RMC 字段完整性校验",
                () -> assertEquals("$GPRMC", rmc.getName()),
                () -> assertEquals("123519.000", rmc.getTime()),
                () -> assertEquals("A", rmc.getStatus()),
                () -> assertEquals("4807.038", rmc.getLatitude()),
                () -> assertEquals("N", rmc.getLatitudeBearing()),
                () -> assertEquals("01131.000", rmc.getLongitude()),
                () -> assertEquals("E", rmc.getLongitudeBearing()),
                () -> assertEquals("022.4", rmc.getSpeed()),
                () -> assertEquals("084.4", rmc.getAzimuth()),
                () -> assertEquals("230394", rmc.getDate()),
                () -> assertEquals("003.1", rmc.getMagneticDeclination()),
                () -> assertEquals("W", rmc.getDirectionOfMagneticDeclination()),
                () -> assertEquals("A", rmc.getMode()),
                () -> assertEquals("*6A", rmc.getCalibrationValue()),
                () -> assertEquals("2026-01-29 16:50:00", rmc.getReadingDate())
        );
    }

    @Test
    @DisplayName("测试 Lombok 生成的 equals 和 hashCode")
    void testEqualsAndHashCode() {
        RmcDTO rmc1 = new RmcDTO();
        rmc1.setName("$GPRMC");
        rmc1.setTime("000001");

        RmcDTO rmc2 = new RmcDTO();
        rmc2.setName("$GPRMC");
        rmc2.setTime("000001");

        RmcDTO rmc3 = new RmcDTO();
        rmc3.setName("$GPRMC");
        rmc3.setTime("999999");

        assertAll("对象相等性逻辑校验",
                () -> assertEquals(rmc1, rmc2, "内容相同的对象应当相等"),
                () -> assertNotEquals(rmc1, rmc3, "内容不同的对象不应当相等"),
                () -> assertEquals(rmc1.hashCode(), rmc2.hashCode(), "相等对象的 HashCode 必须一致")
        );
    }

    @Test
    @DisplayName("测试 toString 包含关键信息")
    void testToString() {
        RmcDTO rmc = new RmcDTO();
        rmc.setName("$GPRMC");
        rmc.setStatus("V"); // 未定位状态

        String result = rmc.toString();
        assertTrue(result.contains("name=$GPRMC"));
        assertTrue(result.contains("status=V"));
    }
}
