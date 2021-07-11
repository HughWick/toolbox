package com.github.hugh.util;

import com.github.hugh.model.dto.GpsDTO;

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
    private static double pi = 3.14159265358979324;

    /**
     * 计算后的Π
     */
    private static double CALC_PI = pi * 3000.0 / 180.0;

    /**
     * 截取小数点后八位
     */
    private static final  DecimalFormat decimalFormat = new DecimalFormat("#.00000000");

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
        double x = longitude - 0.0065, y = latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * CALC_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * CALC_PI);
        // 计算后的gcj02ll 经度
        String gcjLon = decimalFormat.format(z * Math.cos(theta));
        // 计算后的gcj02ll 纬度
        String gcjLat = decimalFormat.format(z * Math.sin(theta));
        return new GpsDTO(Double.parseDouble(gcjLon), Double.parseDouble(gcjLat));
    }
}
