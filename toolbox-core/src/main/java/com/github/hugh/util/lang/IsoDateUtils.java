package com.github.hugh.util.lang;

import com.github.hugh.constant.DateCode;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

/**
 * ISO 8601 灵活日期时间解析与格式化工具类
 *
 * @version 3.0.27
 */
public class IsoDateUtils {

    /**
     * 默认的目标输出格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_TARGET_PATTERN = DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC;
    public static final DateTimeFormatter DEFAULT_TARGET_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_TARGET_PATTERN);

    /**
     * 灵活的 ISO 解析器，支持以下格式：
     * 1. 2021-12-25
     * 2. 2021-12-25T16:53:00
     * 3. 2021-12-25 16:53:00
     * 4. 2021-12-25T16:53:00+08:00
     * 5. 2021-12-25T16:53:00Z
     * 6. 2021-12-25T16:53:00+08:00[Asia/Shanghai]
     */
    private static final DateTimeFormatter FLEXIBLE_ISO_PARSER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart().appendLiteral('T').optionalEnd()
            .optionalStart().appendLiteral(' ').optionalEnd()
            .optionalStart().append(DateTimeFormatter.ISO_LOCAL_TIME).optionalEnd()
            .optionalStart().appendOffsetId().optionalEnd()
            .optionalStart().appendZoneOrOffsetId().optionalEnd()
            // 增加对带中括号大区时区 (如 [Asia/Shanghai]) 的解析支持
            .optionalStart()
            .appendLiteral('[')
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .toFormatter();

    private IsoDateUtils() {
        // 私有化构造器，防止实例化
    }

    public static String formatIsoString(String str) {
        return formatIsoString(str, DEFAULT_TARGET_PATTERN, "", null);
    }
    /**
     * 将 ISO 8601 格式时间字符串转换为标准的 yyyy-MM-dd HH:mm:ss 格式。
     * <p>注意：此方法会忽略输入字符串中的时区信息，直接截取其字面时间。</p>
     * <p>若解析失败，将返回原字符串。</p>
     *
     * @param str 待解析的时间字符串，例如 "2021-12-25T16:53+08:00"
     * @return 格式化后的字符串，例如 "2021-12-25 16:53:00"；若输入为空返回 ""
     */
    public static String formatIsoString(String str, String defaultValue) {
        return formatIsoString(str, DEFAULT_TARGET_PATTERN, defaultValue, null);
    }

    /**
     * 转换为自定义格式（如 yyyy-MM-dd HH:mm）
     *
     * @param str           待解析的时间字符串
     * @param targetPattern 目标输出格式，例如 "yyyy-MM-dd HH:mm"
     * @return 格式化后的字符串；解析失败或输入为空时返回 ""
     */
    public static String formatIsoPattern(String str, String targetPattern) {
        return formatIsoString(str, targetPattern, "", null);
    }

    /**
     * 转换为自定义格式，允许指定默认返回值
     *
     * @param str           待解析的时间字符串
     * @param targetPattern 目标输出格式
     * @param defaultValue  解析失败时的默认返回值
     * @return 格式化后的字符串或默认值
     */
    public static String formatIsoPattern(String str, String targetPattern, String defaultValue) {
        return formatIsoString(str, targetPattern, defaultValue, null);
    }

    /**
     * 通用转换核心方法：支持自定义目标格式、降级默认值以及目标时区转换
     *
     * @param str           待解析的时间字符串
     * @param targetPattern 目标输出格式，传 null 或空时使用默认格式 yyyy-MM-dd HH:mm:ss
     * @param defaultValue  解析失败或输入为空时的默认返回值
     * @param targetZone    目标时区，为 null 则不进行时区转换
     * @return 格式化后的字符串或默认值
     */
    public static String formatIsoString(String str, String targetPattern, String defaultValue, ZoneId targetZone) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue != null ? defaultValue : "";
        }

        try {
            // 获取目标格式化器
            DateTimeFormatter formatter = (targetPattern != null && !targetPattern.trim().isEmpty())
                    ? DateTimeFormatter.ofPattern(targetPattern.trim())
                    : DEFAULT_TARGET_FORMATTER;

            TemporalAccessor parsed = FLEXIBLE_ISO_PARSER.parseBest(
                    str.trim(),
                    ZonedDateTime::from,
                    OffsetDateTime::from,
                    LocalDateTime::from,
                    LocalDate::from
            );

            LocalDateTime resultTime;
            if (parsed instanceof ZonedDateTime) {
                ZonedDateTime zdt = (ZonedDateTime) parsed;
                resultTime = (targetZone != null) ? zdt.withZoneSameInstant(targetZone).toLocalDateTime() : zdt.toLocalDateTime();
            } else if (parsed instanceof OffsetDateTime) {
                OffsetDateTime odt = (OffsetDateTime) parsed;
                resultTime = (targetZone != null) ? odt.atZoneSameInstant(targetZone).toLocalDateTime() : odt.toLocalDateTime();
            } else if (parsed instanceof LocalDateTime) {
                resultTime = (LocalDateTime) parsed;
            } else {
                resultTime = ((LocalDate) parsed).atStartOfDay();
            }
            return resultTime.format(formatter);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
