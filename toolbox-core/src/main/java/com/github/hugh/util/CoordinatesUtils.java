package com.github.hugh.util;


import com.github.hugh.bean.dto.coordinates.GgaDTO;
import com.github.hugh.bean.dto.coordinates.GpsDTO;
import com.github.hugh.bean.dto.coordinates.RmcDTO;
import com.github.hugh.constant.DateCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.regex.RegexUtils;
import org.springframework.util.StringUtils;

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
     * 分隔符
     */
    private static final String comma = ",";
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
     * @since 2.3.12
     */
    public static GpsDTO gcj02ToBd09(final String longitude, final String latitude) {
        return gcj02ToBd09(Double.parseDouble(longitude), Double.parseDouble(latitude));
    }

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
        double bdLongitude = (z * Math.cos(theta) + 0.0065);
        // 计算后的bd09 纬度
        double bdLatitude = z * Math.sin(theta) + 0.006;
        return new GpsDTO(Double.parseDouble(decimalFormat.format(bdLatitude)), Double.parseDouble(decimalFormat.format(bdLongitude)));
    }

    /**
     * 百度转高德
     * <p>百度坐标(bd09ll) 转 火星坐标(gcj02ll)</p>
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return GpsDTO
     * @since 2.3.12
     */
    public static GpsDTO bd09ToGcj02(String longitude, String latitude) {
        return bd09ToGcj02(Double.parseDouble(longitude), Double.parseDouble(latitude));
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
        return new GpsDTO(Double.parseDouble(gcjLat), Double.parseDouble(gcjLon));
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
     *
     * @param degreeMinutes 度分格式:经纬度
     * @return String 经纬度
     * @since 2.3.13
     */
    public static String formatDegreeMinutes(String degreeMinutes) {
        return formatDegreeMinutes(Double.parseDouble(degreeMinutes));
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
     *
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
            throw new ToolboxException("string is null");
        }
        GgaDTO ggaDTO = new GgaDTO();
        String[] arr = gga.split(comma); // 用,分割
        ggaDTO.setName(arr[0]);
        ggaDTO.setDate(arr[1]);
        ggaDTO.setLatitude(arr[2]);
        ggaDTO.setLatitudeBearing(arr[3]);
        ggaDTO.setLongitude(arr[4]);
        ggaDTO.setLongitudeBearing(arr[5]);
        ggaDTO.setGpsStatus(arr[6]);
        ggaDTO.setNumberOfSatellites(arr[7]);
        ggaDTO.setHdopHorizontalAccuracyFactor(arr[8]);
        ggaDTO.setAltitude(arr[9]);
        ggaDTO.setWaterSurfaceAltitude(arr[10]);
        ggaDTO.setDifferentialTime(arr[11]);
        ggaDTO.setDifferentialStationId(arr[12]);
        ggaDTO.setCalibrationValue(arr[14]);
        ggaDTO.setReadingDate(TimeUtils.toCstTime(ggaDTO.getDate(), chooseTimeFormat(ggaDTO.getDate())).toString());
        return ggaDTO;
    }

    /**
     * 根据字符串小数点后几位选择对应的时分秒，格式化格式
     *
     * @param timeString 时分秒格式字符串
     * @return String 时分秒毫秒格式
     */
    private static String chooseTimeFormat(String timeString) {
        int indexOf = timeString.indexOf(".");
        if (indexOf < 0) {
            return DateCode.HOUR_MIN_SEC_FORMAT_SIMPLE;
        }
        int i = timeString.length() - (indexOf + 1);
        String format;
        if (i == 2) {
            format = DateCode.HOUR_MIN_SEC_FORMAT_SIMPLE + ".SS";
        } else if (i == 1) {
            format = DateCode.HOUR_MIN_SEC_FORMAT_SIMPLE + ".S";
        } else {
            format = DateCode.HOUR_MIN_SEC_FORMAT_SIMPLE + ".SSS";
        }
        return format;
    }

    /**
     * 解析 GPRMC 信息成实体
     *
     * @param rmcStr rmc字符串
     * @return RmcDTO
     * @since 2.3.12
     */
    public static RmcDTO parseRmc(String rmcStr) {
        if (EmptyUtils.isEmpty(rmcStr)) {
            throw new ToolboxException("string is null");
        }
        String[] arr = rmcStr.split(comma); // 用,分割
        RmcDTO rmcDTO = new RmcDTO();
        rmcDTO.setName(arr[0]);
        rmcDTO.setTime(arr[1]);
        rmcDTO.setStatus(arr[2]);
        rmcDTO.setLatitude(arr[3]);
        rmcDTO.setLatitudeBearing(arr[4]);
        rmcDTO.setLongitude(arr[5]);
        rmcDTO.setLongitudeBearing(arr[6]);
        rmcDTO.setSpeed(arr[7]);
        rmcDTO.setAzimuth(arr[8]);
        rmcDTO.setDate(arr[9]);
        rmcDTO.setMagneticDeclination(arr[10]);
        rmcDTO.setDirectionOfMagneticDeclination(arr[11]);
        rmcDTO.setMode(arr[11]);
        rmcDTO.setCalibrationValue(arr[12]);
        String dateStr = DateUtils.utcToCst(rmcDTO.getDate() + " " + rmcDTO.getTime(), "ddMMyy HHmmss");
        rmcDTO.setReadingDate(dateStr);
        return rmcDTO;
    }

    /**
     * 计算两点之间距离
     *
     * @param longitudeCommaLatitude1 第一点的经度 纬度,格式：{@code 112.944468,28.218373}
     * @param longitudeCommaLatitude2 第二点的经度 纬度,格式：{@code 112.933732,28.280851}
     * @return double 返回的距离，单位m
     * @since 2.4.8
     */
    public static double getDistance(String longitudeCommaLatitude1, String longitudeCommaLatitude2) {
        if (!longitudeCommaLatitude1.contains(comma) || !longitudeCommaLatitude2.contains(comma)) {
            throw new ToolboxException("latitude and longitude separator does not exist");
        } else if (StringUtils.countOccurrencesOf(longitudeCommaLatitude1, comma) > 1 || StringUtils.countOccurrencesOf(longitudeCommaLatitude2, comma) > 1) {
            throw new ToolboxException("multiple latitude and longitude separators");
        }
        final String[] split1 = longitudeCommaLatitude1.split(comma);
        final String[] split2 = longitudeCommaLatitude2.split(comma);
        return getDistance(split1[0], split1[1], split2[0], split2[1]);
    }

    /**
     * 计算两点之间距离
     *
     * @param long1 第一点的精度
     * @param lat1  第一点的纬度
     * @param long2 第二点的精度
     * @param lat2  第二点的纬度
     * @return double 返回的距离，单位m
     * @since 2.4.8
     */
    public static double getDistance(String long1, String lat1, String long2, String lat2) {
        return getDistance(Double.parseDouble(long1), Double.parseDouble(lat1), Double.parseDouble(long2), Double.parseDouble(lat2));
    }

    /**
     * 计算两点之间距离
     *
     * @param long1 第一点的精度
     * @param lat1  第一点的纬度
     * @param long2 第二点的精度
     * @param lat2  第二点的纬度
     * @return double 返回的距离，单位m
     * @since 2.4.8
     */
    public static double getDistance(double long1, double lat1, double long2, double lat2) {
        if (RegexUtils.isNotLongitude(String.valueOf(long1))) {
            throw new ToolboxException("first longitude error : " + long1);
        } else if (RegexUtils.isNotLongitude(String.valueOf(long2))) {
            throw new ToolboxException("second longitude error : " + long2);
        } else if (RegexUtils.isNotLatitude(String.valueOf(lat1))) {
            throw new ToolboxException("first latitude error : " + lat1);
        } else if (RegexUtils.isNotLatitude(String.valueOf(lat2))) {
            throw new ToolboxException("second latitude error : " + lat2);
        }
        double earthRadius = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        double a = lat1 - lat2;
        double b = (long1 - long2) * Math.PI / 180.0;
        double sa2 = Math.sin(a / 2.0);
        double sb2 = Math.sin(b / 2.0);
        double result = 2 * earthRadius * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return Double.parseDouble(decimalFormat.format(result));
    }
}
