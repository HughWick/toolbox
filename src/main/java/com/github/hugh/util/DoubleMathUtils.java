package com.github.hugh.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * double计算精准工具类
 *
 * @author hugh
 * @since 1.0.1
 */
public class DoubleMathUtils {
    // 这个类不能实例化
    private DoubleMathUtils() {
    }

    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 将double类型强制保留后两位
     */
    public static final NumberFormat numberFormat = NumberFormat.getNumberInstance();

    static {
        int maximum = 2;
        numberFormat.setMaximumFractionDigits(maximum);
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        // 分组: 默认为true 就是 "4,000,000" 这种形式, false 输出= 4000
        numberFormat.setGroupingUsed(false);
    }

    /**
     * 将double类型强制保留后五位、多余的直接舍弃
     */
    public static final NumberFormat numberGiveUp = NumberFormat.getNumberInstance();

    static {
        int maximum = 5;
        numberGiveUp.setMaximumFractionDigits(maximum);
        numberGiveUp.setRoundingMode(RoundingMode.DOWN);
        // 分组: 默认为true 就是 "4,000,000" 这种形式, false 输出= 4000
        numberGiveUp.setGroupingUsed(false);
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 如果是小数，保留两位，非小数，保留整数
     *
     * @param number 浮点数
     * @return String
     */
    public static String getString(double number) {
        String numberStr;
        if (((int) number * 1000) == (int) (number * 1000)) {
            // 如果是一个整数
            numberStr = String.valueOf((int) number);
        } else {
            DecimalFormat df = new DecimalFormat("######0.00");
            numberStr = df.format(number);
        }
        return numberStr;
    }
}
