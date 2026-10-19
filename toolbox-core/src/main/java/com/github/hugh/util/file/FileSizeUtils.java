package com.github.hugh.util.file;

import com.github.hugh.constant.StrPool;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 文件大小格式化工具类
 */
public class FileSizeUtils {

    /**
     * 文件大小单位枚举
     */
    @Getter
    @AllArgsConstructor
    public enum SizeUnit {
        B(1L),
        KB(1024L),
        MB(1024L * 1024L),
        GB(1024L * 1024L * 1024L),
        TB(1024L * 1024L * 1024L * 1024L),
        PB(1024L * 1024L * 1024L * 1024L * 1024L);

        private final long bytes;
    }

    /**
     * 格式化文件大小（支持指定源数值的单位，默认带单位输出）
     *
     * @param size 数值（支持浮点型，如 1.5）
     * @param unit 源数值的单位（如 SizeUnit.KB, SizeUnit.MB）
     * @return 格式化后的字符串
     */
    public static String formatFileSize(double size, SizeUnit unit) {
        return formatFileSize(size, unit, true);
    }

    /**
     * 格式化文件大小（全功能核心底层方法）
     *
     * @param size        数值
     * @param unit        源数值的单位（如果为 null，默认按 B 处理）
     * @param includeUnit 是否包含单位（true 包含单位，false 不包含单位）
     * @return 格式化后的文件大小字符串
     */
    public static String formatFileSize(double size, SizeUnit unit, boolean includeUnit) {
        if (size <= 0) {
            return "0" + (includeUnit ? StrPool.SPACE + "B" : StrPool.EMPTY);
        }

        // 计算该数值对应的总字节数（使用 double 避免溢出）
        SizeUnit sourceUnit = (unit != null) ? unit : SizeUnit.B;
        double bytes = size * sourceUnit.getBytes();

        String[] units = new String[]{"B", "KB", "MB", "GB", "TB", "PB"};
        int unitIndex = 0;

        // 逐步换算到最合适的展示单位
        while (bytes >= 1024 && unitIndex < units.length - 1) {
            bytes /= 1024.0;
            unitIndex++;
        }

        // 如果是 B，保留整数显示
        if (unitIndex == 0) {
            return Math.round(bytes) + (includeUnit ? StrPool.SPACE + units[0] : StrPool.EMPTY);
        }

        // 大于 B 则保留最多 2 位小数
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(bytes) + (includeUnit ? StrPool.SPACE + units[unitIndex] : StrPool.EMPTY);
    }

    /**
     * 格式化文件大小显示，默认包含单位，根据文件大小返回相应的大小字符串（带单位）。
     *
     * @param fileSize 文件大小（字节）
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(long fileSize) {
        return formatFileSize((double) fileSize, SizeUnit.B, true);
    }

    /**
     * 格式化文件大小显示，返回相应的大小字符串（带单位）。
     *
     * @param fileSize    文件大小（字节）
     * @param includeUnit 是否包含单位（true 包含单位，false 不包含单位）
     * @return 格式化后的文件大小字符串
     * @since 2.7.15
     */
    public static String formatFileSize(long fileSize, boolean includeUnit) {
        return formatFileSize((double) fileSize, SizeUnit.B, includeUnit);
    }

    /**
     * 文件大小换算
     *
     * @param file 文件
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return "-1";
        }
        return formatFileSize(file.length());
    }

    /**
     * 根据文件路径，格式化文件大小显示，返回相应的大小字符串（带单位）。
     *
     * @param path 文件路径
     * @return String 格式化后的文件大小：XX MB、XX GB
     * @since 2.2.2
     */
    public static String formatFileSize(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "-1";
        }
        return formatFileSize(new File(path));
    }
}
