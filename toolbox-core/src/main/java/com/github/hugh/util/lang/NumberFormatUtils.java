package com.github.hugh.util.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 高性能数字格式化工具类。
 * <p>
 * 提供了一系列针对浮点数进行四舍五入、保留小数位数的静态方法。
 * 通过预加载 10 的次幂数组，在常规精度要求（0-10位）下显著提升了计算速度，
 * 并消除了运行时的内存开销（0 GC）。
 * </p>
 *
 * @since 3.0.21
 */
public class NumberFormatUtils {
    /**
     * 私有构造函数，防止工具类被实例化。
     */
    private NumberFormatUtils() {
    }
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_UP;
    /**
     * 预加载 10 的次幂常量数组。
     * <p>
     * 缓存了从 1e0 到 1e10 的结果，用于消除 {@link Math#pow(double, double)}
     * 带来的动态计算开销，实现极致的运算速度并避免垃圾回收（0 GC）。
     * </p>
     */
    private static final double[] POWERS_OF_10 = {
            1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9, 1e10
    };

    /**
     * 【核心引擎】通用保留小数位数方法（四舍五入）。
     * <p>
     * 基于预加载的 {@link #POWERS_OF_10} 数组进行极速舍入。
     * 如果指定的精度超出了预加载范围（即小于 0 或大于等于 11），
     * 则会自动降级，使用标准的 {@link Math#pow(double, double)} 进行计算。
     * </p>
     *
     * @param value 原始双精度浮点值，允许为 {@code null}
     * @param scale 需要保留的小数位数（为了获得最佳性能，建议值在 0~10 之间）
     * @return 舍入后的结果。如果输入的值为 {@code null}，则原样返回 {@code null}
     */
    public static Double round(Double value, int scale) {
        if (value == null) return null;
        if (scale < 0 || scale >= POWERS_OF_10.length) {
            // 如果超出范围，降级使用 Math.pow 或者直接返回原值（可根据业务决定）
            double multiplier = Math.pow(10.0, scale);
            return Math.round(value * multiplier) / multiplier;
        }
        double multiplier = POWERS_OF_10[scale];
        return Math.round(value * multiplier) / multiplier;
    }

    /**
     * 保留小数位数方法（单精度浮点数入参，四舍五入）。
     * <p>
     * 此方法为 {@link Float} 类型的重载。将参数转换为 {@link Double} 后，
     * 委托给底层的核心引擎方法进行高速处理。
     * </p>
     *
     * @param value 原始单精度浮点值，允许为 {@code null}
     * @param scale 需要保留的小数位数（为了获得最佳性能，建议值在 0~10 之间）
     * @return 舍入后的双精度浮点数结果。如果输入的值为 {@code null}，则返回 {@code null}
     */
    public static Double round(Float value, int scale) {
        if (value == null) return null;
        // 强转 Double 后调用引擎
        return round(value.doubleValue(), scale);
    }

    /**
     * 格式化数值为字符串，最多保留指定小数位数，并自动去除末尾无意义的 0。
     *
     * @param value    原始数值（支持 Double、Float、BigDecimal 等 {@link Number} 类型），允许为 {@code null}
     * @param maxScale 最多保留的小数位数
     * @param mode     舍入模式（如 {@link RoundingMode#HALF_UP} 四舍五入，或 {@link RoundingMode#DOWN} 直接截断），
     *                 若传入 {@code null} 则默认使用 {@code DEFAULT_ROUNDING}
     * @return 格式化后的字符串。若输入为 {@code null} 或底层转换失败则返回 {@code null}；若为 NaN/Infinity 则原样返回字符串
     * @since 3.0.25
     */
    public static String formatTrimZeros(Number value, int maxScale, RoundingMode mode) {
        if (value == null) return null;
        if (isSpecialFloat(value)) return value.toString();
        BigDecimal bd = toBigDecimal(value);
        if (bd == null) return null;
        RoundingMode rm = (mode != null) ? mode : DEFAULT_ROUNDING;
        return bd.setScale(maxScale, rm).stripTrailingZeros().toPlainString();
    }


    /**
     * 格式化数值为指定小数位数的字符串。若数值舍入后为 0，统一归一化返回整数 "0"。
     * <p>
     * 规则：
     * 1. 若数值舍入后的绝对值为 0（例如 0.00），统一返回 "0"；
     * 2. 若数值非 0，保留固定的 scale 位小数（末尾的 0 会予以保留，如 1.20）；
     * 3. 若输入为 {@code null} 返回 {@code null}，NaN/Infinity 则原样返回。
     * </p>
     *
     * @param value 原始数值（支持 Double、Float、BigDecimal 等 {@link Number} 类型），允许为 {@code null}
     * @param scale 保留的小数位数
     * @param mode  舍入模式（如 {@link RoundingMode#HALF_UP} 四舍五入，或 {@link RoundingMode#DOWN} 直接截断），
     *              若传入 {@code null} 则默认使用 {@code DEFAULT_ROUNDING}
     * @return 格式化后的字符串
     * @since 3.0.25
     */
    public static String formatZeroAsInt(Number value, int scale, RoundingMode mode) {
        if (value == null) return null;
        if (isSpecialFloat(value)) return value.toString();
        BigDecimal bd = toBigDecimal(value);
        if (bd == null) return null;
        RoundingMode rm = (mode != null) ? mode : DEFAULT_ROUNDING;
        bd = bd.setScale(scale, rm);
        if (bd.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return bd.toPlainString();
    }

    /**
     * 安全地将任意 {@link Number} 对象转换为 {@link BigDecimal}。
     * <p>
     * 特殊处理说明：
     * 1. 自动识别并规避 {@link Float} 和 {@link Double} 的精度膨胀陷阱（如 {@code Float} 使用 {@link Float#toString(float)} 构造）；
     * 2. 若输入为 {@code NaN} 或 {@code Infinity}（正/负无穷），统一视作无效数值返回 {@code null}。
     * </p>
     *
     * @param value 原始数值对象（支持 {@link Double}、{@link Float}、{@link BigDecimal}、{@link Long} 等），允许为 {@code null}
     * @return 转换后的 {@link BigDecimal} 对象。若输入为 {@code null}、{@code NaN} 或 {@code Infinity} 则返回 {@code null}
     * @since 3.0.25
     */
    public static BigDecimal toBigDecimal(Number value) {
        if (value == null) return null;
        if (value instanceof Float) {
            float f = (Float) value;
            if (Float.isNaN(f) || Float.isInfinite(f)) return null;
            return new BigDecimal(Float.toString(f));
        }
        if (value instanceof Double) {
            double d = (Double) value;
            if (Double.isNaN(d) || Double.isInfinite(d)) return null;
            return BigDecimal.valueOf(d);
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        return new BigDecimal(value.toString());
    }

    /**
     * 判断指定的 {@link Number} 对象是否为特殊的浮点数值（{@code NaN} 或 {@code Infinity}）。
     *
     * @param value 待检查的数值对象，允许为 {@code null}
     * @return 若对象为 {@link Double} 或 {@link Float} 且值为 {@code NaN}、正无穷或负无穷，则返回 {@code true}；否则返回 {@code false}
     * @since 3.0.25
     */
    private static boolean isSpecialFloat(Number value) {
        if (value instanceof Double) {
            Double aDouble = (Double) value;
            return aDouble.isNaN() || aDouble.isInfinite();
        }
        if (value instanceof Float) {
            Float aFloat = (Float) value;
            return aFloat.isNaN() || aFloat.isInfinite();
        }
        return false;
    }

    /**
     * 格式化数值并去除末尾多余的零（指定最大小数位数，使用默认舍入模式）。
     *
     * @param value    需要格式化的数值
     * @param maxScale 最多保留的小数位数
     * @return 格式化后的字符串（例如：1.20 会被转换为 "1.2"）
     * @since 3.0.25
     */
    public static String formatTrimZeros(Number value, int maxScale) {
        return formatTrimZeros(value, maxScale, DEFAULT_ROUNDING);
    }

    /**
     * 格式化数值并去除末尾多余的零（默认最多保留 2 位小数，使用默认舍入模式）。
     *
     * @param value 需要格式化的数值
     * @return 格式化后的字符串
     * @since 3.0.25
     */
    public static String formatTrimZeros(Number value) {
        return formatTrimZeros(value, 2, DEFAULT_ROUNDING);
    }

    /**
     * 格式化数值，若数值等于 0 则直接展示为整数 "0"（指定小数位数，使用默认舍入模式）。
     *
     * @param value 需要格式化的数值
     * @param scale 保留的小数位数
     * @return 格式化后的字符串（例如：0.00 会被转换为 "0"，非零值如 1.20 则保留对应位数）
     * @since 3.0.25
     */
    public static String formatZeroAsInt(Number value, int scale) {
        return formatZeroAsInt(value, scale, DEFAULT_ROUNDING);
    }

    /**
     * 格式化数值，若数值等于 0 则直接展示为整数 "0"（默认保留 2 位小数，使用默认舍入模式）。
     *
     * @param value 需要格式化的数值
     * @return 格式化后的字符串
     * @since 3.0.25
     */
    public static String formatZeroAsInt(Number value) {
        return formatZeroAsInt(value, 2, DEFAULT_ROUNDING);
    }
}
