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

    @Nested
    @DisplayName("1. ISO 8601 多种合法格式解析测试")
    class FlexibleIsoParsingTest {

        @Test
        @DisplayName("1.1 纯日期格式 (yyyy-MM-dd)")
        void testLocalDateOnly() {
            String input = "2021-12-25";
            String result = IsoDateUtils.format(input);
            assertEquals("2021-12-25 00:00:00", result);
        }
    }

    @Nested
    @DisplayName("4. 重载方法及代码覆盖率补充")
    class OverloadMethodTest {

        @Test
        @DisplayName("4.1 单参数方法 formatIsoString(str)")
        void testSingleParamMethod() {
            String input = "2021-12-25T16:53:00";
            String result = IsoDateUtils.format(input);
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
            String result = IsoDateUtils.format(input, pattern);
            assertEquals("2021-12-25 16:53", result);
        }

        @Test
        @DisplayName("5.2 格式化为斜杠分隔日期 yyyy/MM/dd")
        void testCustomPatternSlashDate() {
            String input = "2021-12-25 16:53:00";
            String pattern = "yyyy/MM/dd";
            String result = IsoDateUtils.format(input, pattern);
            assertEquals("2021/12/25", result);
        }

        @Test
        @DisplayName("5.3 格式化为中文年月日 yyyy年MM月dd日 HH:mm:ss")
        void testCustomPatternChineseFormat() {
            String input = "2021-12-25T16:53:00Z";
            String pattern = "yyyy年MM月dd日 HH:mm:ss";
            String result = IsoDateUtils.format(input, pattern);
            assertEquals("2021年12月25日 16:53:00", result);
        }

        @Test
        @DisplayName("5.4 纯日期输入结合 yyyy-MM-dd HH:mm 格式输出（自动补全 00:00）")
        void testLocalDateToCustomPattern() {
            String input = "2021-12-25";
            String pattern = "yyyy-MM-dd HH:mm";
            String result = IsoDateUtils.format(input, pattern);
            assertEquals("2021-12-25 00:00", result);
        }
    }
}
