package com.github.hugh.util.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * IsoDateUtils 工具类测试
 */
@DisplayName("IsoDateUtils 测试套件")
class IsoDateUtilsTest {

//    private static final String DEFAULT_VALUE = "DEFAULT_DATE";

    @Nested
    @DisplayName("1. ISO 8601 多种合法格式解析测试")
    class FlexibleIsoParsingTest {

        @Test
        @DisplayName("1.1 纯日期格式 (yyyy-MM-dd)")
        void testLocalDateOnly() {
            String input = "2021-12-25";
            String result = IsoDateUtils.formatIsoString(input);
            assertEquals("2021-12-25 00:00:00", result);
        }

//        @Test
//        @DisplayName("1.2 带 'T' 分隔符的日期时间 (yyyy-MM-ddTHH:mm:ss)")
//        void testLocalDateTimeWithT() {
//            String input = "2021-12-25T16:53:00";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("1.3 带空格分隔符的日期时间 (yyyy-MM-dd HH:mm:ss)")
//        void testLocalDateTimeWithSpace() {
//            String input = "2021-12-25 16:53:00";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("1.4 带偏移量的 ISO 字符串 (OffsetDateTime)")
//        void testOffsetDateTime() {
//            String input = "2021-12-25T16:53:00+08:00";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("1.5 带 UTC 'Z' 的 ISO 字符串")
//        void testUtcDateTime() {
//            String input = "2021-12-25T16:53:00Z";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("1.6 带大区时区的 ISO 字符串 (ZonedDateTime)")
//        void testZonedDateTimeWithRegion() {
//            String input = "2021-12-25T16:53:00+08:00[Asia/Shanghai]";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
    }

//    @Nested
//    @DisplayName("2. 时区转换测试 (targetZone)")
//    class TimeZoneConversionTest {
//
//        @Test
//        @DisplayName("2.1 UTC 转为 +08:00 时区 (Asia/Shanghai)")
//        void testConvertUtcToShanghai() {
//            String input = "2021-12-25T16:53:00Z";
//            ZoneId targetZone = ZoneId.of("Asia/Shanghai"); // UTC+8
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE, targetZone);
//            assertEquals("2021-12-26 00:53:00", result);
//        }
//
//        @Test
//        @DisplayName("2.2 +08:00 转为 UTC 时区")
//        void testConvertShanghaiToUtc() {
//            String input = "2021-12-25T16:53:00+08:00";
//            ZoneId targetZone = ZoneId.of("UTC");
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE, targetZone);
//            assertEquals("2021-12-25 08:53:00", result);
//        }
//
//        @Test
//        @DisplayName("2.3 不带时区信息的输入传入 targetZone（忽略转换）")
//        void testNoZoneInfoWithTargetZone() {
//            String input = "2021-12-25 16:53:00";
//            ZoneId targetZone = ZoneId.of("UTC");
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE, targetZone);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("2.4 targetZone 为 null 时保留原字面时间")
//        void testNullTargetZone() {
//            String input = "2021-12-25T16:53:00+08:00";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE, null);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//    }

//    @Nested
//    @DisplayName("3. 边界、异常及容错测试")
//    class EdgeAndExceptionTest {

//        @ParameterizedTest
//        @NullAndEmptySource
//        @DisplayName("3.1 输入为 null 或空/纯空格字符串，应返回空字符串")
//        void testNullOrBlankInput(String input) {
//            String result = IsoDateUtils.formatIsoString(input);
//            assertEquals(null, result);
//        }

//        @Test
//        @DisplayName("3.2 输入带首尾空格，应正常解析")
//        void testInputWithTrimming() {
//            String input = "  2021-12-25T16:53:00  ";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals("2021-12-25 16:53:00", result);
//        }
//
//        @Test
//        @DisplayName("3.3 非法格式输入，应返回指定的 defaultValue")
//        void testInvalidFormatReturnsDefaultValue() {
//            String input = "invalid-date-string";
//            String result = IsoDateUtils.formatIsoString(input, DEFAULT_VALUE);
//            assertEquals(DEFAULT_VALUE, result);
//        }
//    }

    @Nested
    @DisplayName("4. 重载方法及代码覆盖率补充")
    class OverloadMethodTest {

        @Test
        @DisplayName("4.1 单参数方法 formatIsoString(str)")
        void testSingleParamMethod() {
            String input = "2021-12-25T16:53:00";
            String result = IsoDateUtils.formatIsoString(input);
            assertEquals("2021-12-25 16:53:00", result);
        }

        @Test
        @DisplayName("4.2 测试私有构造函数（反射触发，实现 100% 覆盖）")
        void testPrivateConstructor() throws Exception {
            Constructor<IsoDateUtils> constructor = IsoDateUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            IsoDateUtils instance = constructor.newInstance();
            assertNotNull(instance);
        }
    }

    @Nested
    @DisplayName("5. 自定义目标格式测试")
    class CustomPatternTest {

        @Test
        @DisplayName("5.1 格式化为 yyyy-MM-dd HH:mm (不带秒)")
        void testCustomPatternNoSeconds() {
            String input = "2021-12-25T16:53:20+08:00";
            String pattern = "yyyy-MM-dd HH:mm";
            String result = IsoDateUtils.formatIsoPattern(input, pattern);
            assertEquals("2021-12-25 16:53", result);
        }

        @Test
        @DisplayName("5.2 格式化为斜杠分隔日期 yyyy/MM/dd")
        void testCustomPatternSlashDate() {
            String input = "2021-12-25 16:53:00";
            String pattern = "yyyy/MM/dd";
            String result = IsoDateUtils.formatIsoPattern(input, pattern);
            assertEquals("2021/12/25", result);
        }

        @Test
        @DisplayName("5.3 格式化为中文年月日 yyyy年MM月dd日 HH:mm:ss")
        void testCustomPatternChineseFormat() {
            String input = "2021-12-25T16:53:00Z";
            String pattern = "yyyy年MM月dd日 HH:mm:ss";
            String result = IsoDateUtils.formatIsoPattern(input, pattern);
            assertEquals("2021年12月25日 16:53:00", result);
        }

        @Test
        @DisplayName("5.4 纯日期输入结合 yyyy-MM-dd HH:mm 格式输出（自动补全 00:00）")
        void testLocalDateToCustomPattern() {
            String input = "2021-12-25";
            String pattern = "yyyy-MM-dd HH:mm";
            String result = IsoDateUtils.formatIsoPattern(input, pattern);
            assertEquals("2021-12-25 00:00", result);
        }

//        @Test
//        @DisplayName("5.5 自定义格式结合时区转换 (UTC 转 +08:00 并按 yyyy-MM-dd HH:mm 输出)")
//        void testCustomPatternWithTimeZone() {
//            String input = "2021-12-25T16:53:00Z";
//            String pattern = "yyyy-MM-dd HH:mm";
//            ZoneId targetZone = ZoneId.of("Asia/Shanghai");
//            String result = IsoDateUtils.formatIsoString(input, pattern, "DEFAULT", targetZone);
//            assertEquals("2021-12-26 00:53", result);
//        }
//
//        @Test
//        @DisplayName("5.6 传入非法 targetPattern，应捕获异常并返回 defaultValue")
//        void testInvalidTargetPatternReturnsDefaultValue() {
//            String input = "2021-12-25T16:53:00";
//            String invalidPattern = "illegal-pattern-xyz";
//            String result = IsoDateUtils.formatIsoPattern(input, invalidPattern, "DEFAULT");
//            assertEquals("DEFAULT", result);
//        }
    }
}
