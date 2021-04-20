package com.github.hugh.util;

import java.text.DecimalFormat;

/**
 * 坐标工具类
 * <p>
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
     * 高德转百度
     * <p>火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 坐标的转换算法</p>
     * <p>注：只保留小数点后八位</p>
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return double[] {@code {经度,纬度}}
     * @since 1.6.3
     */
    public static double[] gcj02ToBd09(final double longitude, final double latitude) {
        double calcPi = pi * 3000.0 / 180.0;
        double z = Math.sqrt(longitude * longitude + latitude * latitude) + 0.00002 * Math.sin(latitude * calcPi);
        double theta = Math.atan2(latitude, longitude) + 0.000003 * Math.cos(longitude * calcPi);
        // 计算后的bd09 经度
        double bdlon = z * Math.cos(theta) + 0.0065;
        // 金酸后的bd09 纬度
        double bdlat = z * Math.sin(theta) + 0.006;
        DecimalFormat df = new DecimalFormat("#.00000000");
        double[] bd_lat_lon = new double[2];
        bd_lat_lon[0] = Double.parseDouble(df.format(bdlon));
        bd_lat_lon[1] = Double.parseDouble(df.format(bdlat));
        return bd_lat_lon;
    }
}
