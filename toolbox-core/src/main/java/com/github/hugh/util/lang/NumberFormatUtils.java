package com.github.hugh.util.lang;

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

    /**
     * 预加载 10 的次幂常量数组。
     * <p>
     * 缓存了从 1e0 到 1e10 的结果，用于消除 {@link Math#pow(double, double)}
     * 带来的动态计算开销，实现极致的运算速度并避免垃圾回收（0 GC）。
     * </p>
     */
    private static final double[] POWERS_OF_10 = {
            1e0,  1e1,  1e2,  1e3,  1e4,  1e5,  1e6,  1e7,  1e8,  1e9,  1e10
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
}
