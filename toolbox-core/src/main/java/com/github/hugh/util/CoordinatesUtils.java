package com.github.hugh.util;

import com.github.hugh.model.dto.GgaDTO;
import com.github.hugh.model.dto.GpsDTO;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 坐标工具类
 *
 * @author hugh
 * @since 1.6.3
 */
public class CoordinatesUtils {

    /**
     * 圆周率
     */
    private static final double pi = 3.14159265358979324;

    /**
     * 计算后的Π
     */
    private static final double CALC_PI = pi * 3000.0 / 180.0;

    /**
     * 截取小数点后八位
     */
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00000000");

    /**
     * 高德转百度
     * <p>火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 坐标的转换算法</p>
     * <p>注：只保留小数点后八位</p>
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return {@link GpsDTO}
     * @since 1.6.3
     */
    public static GpsDTO gcj02ToBd09(final double longitude, final double latitude) {
        double z = Math.sqrt(longitude * longitude + latitude * latitude) + 0.00002 * Math.sin(latitude * CALC_PI);
        double theta = Math.atan2(latitude, longitude) + 0.000003 * Math.cos(longitude * CALC_PI);
        // 计算后的bd09 经度
        double bdlon = (z * Math.cos(theta) + 0.0065);
        // 金酸后的bd09 纬度
        double bdlat = z * Math.sin(theta) + 0.006;
        return new GpsDTO(Double.parseDouble(decimalFormat.format(bdlat)), Double.parseDouble(decimalFormat.format(bdlon)));
    }

    /**
     * 百度转高德
     * <p>百度坐标(bd09ll) 转 火星坐标(gcj02ll)</p>
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return GpsDTO  {@link GpsDTO}
     * @since 1.6.4
     */
    public static GpsDTO bd09ToGcj02(double longitude, double latitude) {
        double x = longitude - 0.0065;
        double y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * CALC_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * CALC_PI);
        // 计算后的gcj02ll 经度
        String gcjLon = decimalFormat.format(z * Math.cos(theta));
        // 计算后的gcj02ll 纬度
        String gcjLat = decimalFormat.format(z * Math.sin(theta));
        return new GpsDTO(Double.parseDouble(gcjLon), Double.parseDouble(gcjLat));
    }

    /**
     * 度分格式的gga经纬度换算成wgs84的经纬度
     * <p>
     * 算法示例：
     * <p>
     * 东经：10629.6601-{@code >}106.29.6601-{@code >}106+29.6601÷60=106.494335°
     * <p>
     * 北纬：2937.1526-{@code >}29.37.1526-29+37.1526÷60=29.61921°
     * </p>
     * @param degreeMinutes 度分格式:经纬度
     * @return String 经纬度
     * @since 1.7.4
     */
    public static String formatDegreeMinutes(double degreeMinutes) {
        // 向下取整
        double number = Math.floor(degreeMinutes / 100);
        String number2 = String.valueOf(degreeMinutes / 100);
        // 获取小数点后的数
        number2 = number2.substring(number2.indexOf("."));
        double v = Double.parseDouble("0" + number2) * 100;
        double number3 = DoubleMathUtils.div(v, 60, 8);
        BigDecimal b1 = new BigDecimal(Double.toString(number));
        BigDecimal b2 = new BigDecimal(Double.toString(number3));
        return b2.add(b1).toString();
    }

    /**
     * gga解析成实体
     *
     * @param gga gga信息
     * @return GgaDTO
     * @since 1.7.4
     */
    public static GgaDTO parseGga(String gga) {
        if (EmptyUtils.isEmpty(gga)) {
            return null;
        }
        GgaDTO ggaDTO = new GgaDTO();
        String[] arr = gga.split(","); // 用,分割
        for (int i = 0; i < arr.length; i++) {
            String str = arr[i];
            switch (i) {
                case 0:
                    ggaDTO.setName(str);
                    break;
                case 1:
                    ggaDTO.setDate(str);
                    break;
                case 2:
                    ggaDTO.setLatitude(str);
                    break;
                case 3:
                    ggaDTO.setLatitudeBearing(str);
                    break;
                case 4:
                    ggaDTO.setLongitude(str);
                    break;
                case 5:
                    ggaDTO.setLongitudeBearing(str);
                    break;
                case 6:
                    ggaDTO.setGpsStatus(str);
                    break;
                case 7:
                    ggaDTO.setNumberOfSatellites(str);
                    break;
                case 8:
                    ggaDTO.setHdopHorizontalAccuracyFactor(str);
                    break;
                case 9:
                    ggaDTO.setAltitude(str);
                    break;
                case 10:
                    ggaDTO.setWaterSurfaceAltitude(str);
                    break;
                case 11:
                    ggaDTO.setDifferentialTime(str);
                    break;
                case 12:
                    ggaDTO.setDifferentialStationId(str);
                    break;
                default:
                    break;
            }
        }
        ggaDTO.setCalibrationValue(arr[arr.length - 1]);
        return ggaDTO;
    }
}
