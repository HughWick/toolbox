package com.github.hugh.util;


import com.github.hugh.bean.dto.coordinates.GgaDTO;
import com.github.hugh.bean.dto.coordinates.GpsDTO;
import com.github.hugh.bean.dto.coordinates.RmcDTO;
import com.github.hugh.constant.DateCode;
import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.regex.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 坐标工具类
 *
 * @author hugh
 * @since 1.6.3
 */
@Slf4j
public class CoordinatesUtils {

    /**
     * 圆周率
     */
    private static final double PI = 3.1415926535897932384626433832795;

    /**
     * 计算后的Π
     */
    private static final double CALC_PI = PI * 3000.0 / 180.0;


    // WGS-84 椭球模型参数
    private static final double WGS84_SEMI_MAJOR_AXIS = 6378245; // 长半轴
    private static final double WGS84_FLATTENING = 0.00669342162296594323D; // 扁率


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
        String[] arr = gga.split(StrPool.COMMA); // 用,分割
        // 如果分割后的数组长度不够，给出适当的提示或处理
        if (arr.length < 15) {
            log.error("Invalid GGA string，data：{}", gga);
            throw new ToolboxException("Invalid GGA string, insufficient data.");
        }
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
        String[] arr = rmcStr.split(StrPool.COMMA); // 用,分割
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
        if (!longitudeCommaLatitude1.contains(StrPool.COMMA) || !longitudeCommaLatitude2.contains(StrPool.COMMA)) {
            throw new ToolboxException("latitude and longitude separator does not exist");
        } else if (StringUtils.countOccurrencesOf(longitudeCommaLatitude1, StrPool.COMMA) > 1 || StringUtils.countOccurrencesOf(longitudeCommaLatitude2, StrPool.COMMA) > 1) {
            throw new ToolboxException("multiple latitude and longitude separators");
        }
        final String[] split1 = longitudeCommaLatitude1.split(StrPool.COMMA);
        final String[] split2 = longitudeCommaLatitude2.split(StrPool.COMMA);
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

    /**
     * 将度分秒 (DMS) 格式的经纬度字符串转换为十进制度 (Decimal Degrees - DD)。
     * 示例输入: "36°17'51\"N", "102°53'42\"E"
     * 请注意：这个方法只处理数值和方向，不处理复杂的输入格式校验。
     *
     * @param dmsCoord 度分秒格式的坐标字符串，例如 "36°17'51\"N"
     * @return 转换后的十进制度
     * @since 3.0.5
     */
    public static double dmsToGps(String dmsCoord) {
        // 移除度、分、秒符号，将它们替换为空格以方便后续分割。
        // StrPool.SPACE 应该代表 " "，StrPool.EMPTY 应该代表 ""。
        String cleaned = dmsCoord.replace("°", StrPool.SPACE)
                .replace("'", StrPool.SPACE)
                .replace("\"", StrPool.EMPTY);
        // 提取方向 (N, S, E, W)
        char direction = Character.toUpperCase(cleaned.charAt(cleaned.length() - 1));
        if (direction != 'N' && direction != 'S' && direction != 'E' && direction != 'W') {
            throw new IllegalArgumentException("Invalid direction in DMS format: " + dmsCoord + ". Expected N, S, E, or W.");
        }
        // 移除方向，留下纯数字部分
        double decimalDegrees = getDecimalDegrees(dmsCoord, cleaned);
        // 根据方向调整符号
        if (direction == 'S' || direction == 'W') {
            decimalDegrees = -decimalDegrees;
        }
        return decimalDegrees;
    }

    /**
     * 辅助方法：从清理后的 DMS 字符串中提取数字部分并计算十进制度。
     * 这个方法假设清理后的字符串格式为 "度 分 秒 方向"（例如 "36 17 51N" 经过清理后只剩数字和方向，方向会在传入前被截取）。
     * 它将数字部分按空格分割为度、分、秒，并进行转换计算。
     *
     * @param dmsCoord 原始 DMS 格式的坐标字符串，用于在错误信息中提供上下文。
     * @param cleaned  已经移除了度、分、秒符号的字符串，例如 "36 17 51N"。
     * @return 计算出的十进制度数值（未考虑方向符号）。
     * @since 3.0.5
     */
    private static double getDecimalDegrees(String dmsCoord, String cleaned) {
        String numericParts = cleaned.substring(0, cleaned.length() - 1).trim();
        // 分割字符串获取度、分、秒
        // 使用一个或多个空格作为分隔符
        String[] parts = numericParts.split("\\s+");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid DMS format: " + dmsCoord + ". Expected Degrees Minutes Seconds.");
        }
        double degrees, minutes, seconds;
        try {
            degrees = Double.parseDouble(parts[0]);
            minutes = Double.parseDouble(parts[1]);
            seconds = Double.parseDouble(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in DMS string: " + dmsCoord, e);
        }
        return degrees + (minutes / 60.0) + (seconds / 3600.0);
    }

    /**
     * 将 WGS-84 坐标转换为 GCJ-02 (火星坐标系) 坐标。
     * <p>
     * 这是主要的入口方法。
     *
     * @param longitude WGS-84 经度
     * @param latitude  WGS-84 纬度
     * @return 转换后的 GCJ-02 坐标对象
     * @since 3.0.12
     */
    public static GpsDTO wgs84ToGcj02(double longitude, double latitude) {
        // 如果坐标在中国境外，则无需转换，直接返回原始坐标
        if (outOfChina(longitude, latitude)) {
            return new GpsDTO(latitude, longitude);
        }
        // 计算偏移量
        GpsDTO offset = calculateOffset(longitude, latitude, true);
        // 将偏移量应用到原始坐标上得到 GCJ-02 坐标
        return new GpsDTO(latitude + offset.getLatitude(), longitude + offset.getLongitude());
    }

    /**
     * 将 GCJ-02 (火星坐标系) 坐标转换为 WGS-84 坐标。
     * <p>
     * 采用近似法，对于大多数应用场景精度足够。
     *
     * @param longitude GCJ-02 经度
     * @param latitude  GCJ-02 纬度
     * @return 转换后的 WGS-84 坐标对象
     * @since 3.0.12
     */
    public static GpsDTO gcj02ToWgs84(double longitude, double latitude) {
        if (outOfChina(longitude, latitude)) {
            return new GpsDTO(latitude, longitude);
        }
        GpsDTO offset = calculateOffset(longitude, latitude, false);
        return new GpsDTO(latitude + offset.getLatitude(), longitude + offset.getLongitude());
    }

    /**
     * 判断给定的经纬度是否在中国境外。
     * GCJ-02 坐标系仅对中国大陆和港澳地区有效。
     *
     * @param lng 经度
     * @param lat 纬度
     * @return 如果在中国境外则返回 true，否则返回 false
     * @since 3.0.12
     */
    public static boolean outOfChina(double lng, double lat) {
        return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271);
    }

    /**
     * 计算 WGS-84 到 GCJ-02 的偏移量。
     *
     * @param lng    经度
     * @param lat    纬度
     * @param isPlus true 表示正向偏移 (WGS-84 -> GCJ-02)，false 表示反向偏移 (GCJ-02 -> WGS-84)
     * @return 包含经纬度偏移量的坐标对象
     * @since 3.0.12
     */
    private static GpsDTO calculateOffset(double lng, double lat, boolean isPlus) {
        // 计算经纬度相对于中国基准点(105, 35)的差值
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        // 将纬度的弧度值作为后续计算的参数
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - WGS84_FLATTENING * magic * magic;
        final double sqrtMagic = Math.sqrt(magic);
        // 将计算出的米制偏移转换为经纬度偏移
        dLng = (dLng * 180.0) / (WGS84_SEMI_MAJOR_AXIS / sqrtMagic * Math.cos(radLat) * PI);
        dLat = (dLat * 180.0) / ((WGS84_SEMI_MAJOR_AXIS * (1 - WGS84_FLATTENING)) / (magic * sqrtMagic) * PI);
        // 如果是反向转换，则将偏移量取反
        if (!isPlus) {
            dLng = -dLng;
            dLat = -dLat;
        }
        return new GpsDTO(dLat, dLng);
    }

    /**
     * 计算经度偏移量。这是一个经验公式。
     *
     * @param lng 经度与基准经度之差
     * @param lat 纬度与基准纬度之差
     * @return 经度偏移量
     * @since 3.0.12
     */
    private static double transformLng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 计算纬度偏移量。这是一个经验公式。
     *
     * @param lng 经度与基准经度之差
     * @param lat 纬度与基准纬度之差
     * @return 纬度偏移量
     * @since 3.0.12
     */
    private static double transformLat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }
}
