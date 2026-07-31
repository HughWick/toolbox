package com.github.hugh;

import com.github.hugh.util.UserAgentUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserAgentUtils 工具类单元测试")
class UserAgentUtilsTest {

    private MockHttpServletRequest request;
    private static final String USER_AGENT = "User-Agent";
    private static final String UNKNOWN = "Unknown";
    // 常用测试用例 User-Agent 字符串
    private static final String UA_CHROME_WIN = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String UA_IPHONE_SAFARI = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5 Mobile/15E148 Safari/604.1";
    private static final String UA_ANDROID_HUAWEI = "Mozilla/5.0 (Linux; Android 12; HARMONYOS; ALN-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.88 Mobile Safari/537.36";
    private static final String UA_GOOGLE_BOT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    // 各种真实的 User-Agent 样本
    private static final String UA_CHROME_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
//    private static final String UA_CHROME_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36";
    private static final String UA_WECHAT_IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.38(0x1800262c) NetType/WIFI Language/zh_CN";
    private static final String UA_WECHAT_MINI_PROGRAM = "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.38(0x1800262c) NetType/WIFI Language/zh_CN miniProgram";
    private static final String UA_ALIPAY_ANDROID = "Mozilla/5.0 (Linux; U; Android 13; zh-cn; KB2000 Build/TP1A) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/100.0.4896.58 Mobile Safari/537.36 AlipayDefined(nt:WIFI,ws:1080|2322) AlipayClient/10.3.96.8000 Language/zh-Hans";
    private static final String UA_DING_TALK = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/20A362 DingTalk/7.0.30.7 zh-Hans-CN UT4Aplus/0.0.6";
    // 微博客户端 UA 示例
    private static final String UA_WEIBO = "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 Weibo (iPhone10,4__weibo__11.10.1__iphone__os15.0)";

    // 抖音客户端 UA 示例（内嵌 Aweme 标识）
    private static final String UA_DOU_YIN = "Mozilla/5.0 (Linux; Android 11; Pixel 5 Build/RD2A.210905.003; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/92.0.4515.131 Mobile Safari/537.36 aweme_17.9.0";
    // 补充企业微信 User-Agent 示例（包含 wxwork 关键字）
    private static final String UA_WXWORK_IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_5 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.38(0x1800262c) NetType/WIFI Language/zh_CN wxwork/4.1.6 (MicroMessenger/6.2.0) MacWechat/store";
    // 企业微信 Android 客户端 User-Agent 示例（包含 wxwork 关键字）
    private static final String UA_WXWORK_ANDROID = "Mozilla/5.0 (Linux; Android 12; SM-G991B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.99 Mobile Safari/537.36 MicroMessenger/8.0.28 wxwork/4.0.18";
    private static final String UA_ANDROID = "Mozilla/5.0 (Linux; Android 14; SM-S918B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36";
    private static final String UA_MAC = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String UA_WINDOWS_NT6_1 = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Nested
    @DisplayName("1. 边界条件与 Null 防护测试")
    class BoundaryTests {

        @Test
        @DisplayName("当 request 为 null 时，所有方法应优雅降级返回 Unknown 或默认值，不抛异常")
        void testNullRequest() {
            assertEquals("Unknown/Unknown", UserAgentUtils.getBrowserName(null));
            assertEquals("Unknown", UserAgentUtils.getOsName(null));
            assertEquals("Unknown", UserAgentUtils.getBrowserVersion(null));
            assertNull(UserAgentUtils.getUserAgent(null));
            assertTrue(UserAgentUtils.getHeaders(null).isEmpty());
            assertEquals("Unknown", UserAgentUtils.getOsNameAndVersion(null));
            assertEquals("Unknown", UserAgentUtils.getDeviceClass(null));
            assertFalse(UserAgentUtils.isMobile(null));
            assertFalse(UserAgentUtils.isRobot(null));
            assertEquals("Unknown", UserAgentUtils.getDeviceName(null));
            assertEquals("Unknown", UserAgentUtils.getLayoutEngine(null));
//            assertEquals("Unknown", UserAgentUtils.getClientIp(null));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "\t\n"})
        @DisplayName("当 User-Agent 头为空白字符串时，应统一识别为 Unknown")
        void testBlankUserAgent(String blankUa) {
            request.addHeader("User-Agent", blankUa);

            assertEquals("Unknown/Unknown", UserAgentUtils.getBrowserName(request));
            assertEquals("Unknown", UserAgentUtils.getOsName(request));
            assertEquals("Unknown", UserAgentUtils.getBrowserVersion(request));
            assertEquals(blankUa, UserAgentUtils.getUserAgent(request));
            assertEquals("Unknown", UserAgentUtils.getOsNameAndVersion(request));
            assertEquals("Unknown", UserAgentUtils.getDeviceClass(request));
            assertFalse(UserAgentUtils.isMobile(request));
            assertFalse(UserAgentUtils.isRobot(request));
        }
    }

    @Nested
    @DisplayName("2. 桌面端 PC 解析测试")
    class DesktopTests {

        @Test
        @DisplayName("准确解析 Windows 10 + Chrome 120 浏览器")
        void testChromeOnWindows() {
            request.addHeader("User-Agent", UA_CHROME_WIN);

            assertEquals("Chrome/120", UserAgentUtils.getBrowserName(request));
            assertEquals("Windows", UserAgentUtils.getOsName(request));
            assertEquals("120", UserAgentUtils.getBrowserVersion(request));
            assertEquals("Desktop", UserAgentUtils.getDeviceClass(request));
            assertEquals("Blink", UserAgentUtils.getLayoutEngine(request));
            assertFalse(UserAgentUtils.isMobile(request));
            assertFalse(UserAgentUtils.isRobot(request));
        }
    }

    @Nested
    @DisplayName("3. 移动端设备解析测试")
    class MobileTests {

        @Test
        @DisplayName("准确解析 iPhone + Mobile Safari")
        void testIPhoneSafari() {
            request.addHeader("User-Agent", UA_IPHONE_SAFARI);

            assertEquals("iOS", UserAgentUtils.getOsName(request));
            assertEquals("Phone", UserAgentUtils.getDeviceClass(request));
            assertTrue(UserAgentUtils.isMobile(request));
            assertFalse(UserAgentUtils.isRobot(request));
            assertEquals("Apple iPhone", UserAgentUtils.getDeviceName(request));
        }

        @Test
        @DisplayName("准确识别 Android / 鸿蒙设备")
        void testAndroidDevice() {
            request.addHeader("User-Agent", UA_ANDROID_HUAWEI);

            assertTrue(UserAgentUtils.isMobile(request));
            assertEquals("Phone", UserAgentUtils.getDeviceClass(request));
            assertFalse(UserAgentUtils.isRobot(request));
        }
    }

    @Nested
    @DisplayName("4. 爬虫与网络机器人测试")
    class RobotTests {

        @Test
        @DisplayName("准确识别 Googlebot 爬虫")
        void testGoogleBot() {
            request.addHeader("User-Agent", UA_GOOGLE_BOT);

            assertTrue(UserAgentUtils.isRobot(request));
            assertFalse(UserAgentUtils.isMobile(request));
            assertEquals("Robot", UserAgentUtils.getDeviceClass(request));
        }
    }

    @Nested
    @DisplayName("6. Header 字典映射测试")
    class HeaderTests {

        @Test
        @DisplayName("正确解析 Request Header 到 Map 容器")
        void testGetHeaders() {
            request.addHeader("Authorization", "Bearer eyJhbGciOi...");
            request.addHeader("Accept", "application/json");

            Map<String, String> headers = UserAgentUtils.getHeaders(request);

            assertEquals(2, headers.size());
            assertEquals("Bearer eyJhbGciOi...", headers.get("Authorization"));
            assertEquals("application/json", headers.get("Accept"));
        }
    }

    @Test
    void testStandardBrowser() {
        request.addHeader("User-Agent", UA_CHROME_WINDOWS);
        assertEquals("Chrome/120", UserAgentUtils.getBrowserName(request));
        assertEquals("Windows 10/11", UserAgentUtils.getOsNameAndVersion(request));
        assertEquals("Desktop", UserAgentUtils.getDeviceClass(request));
        assertFalse(UserAgentUtils.isMobile(request));
        assertFalse(UserAgentUtils.isRobot(request));
        assertEquals("Other", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testWechatApp() {
        request.addHeader("User-Agent", UA_WECHAT_IOS);
        assertTrue(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isWechat(request));
        assertFalse(UserAgentUtils.isWechatMiniProgram(request)); // 普通微信不是小程序
        assertEquals("Wechat", UserAgentUtils.getDomesticPlatform(request));

        // Yauaa 对于微信一般会识别底层系统
        assertTrue(UserAgentUtils.getOsNameAndVersion(request).startsWith("iOS"));
    }

    @Test
    void testWechatMiniProgram() {
        request.addHeader("User-Agent", UA_WECHAT_MINI_PROGRAM);
        assertTrue(UserAgentUtils.isWechat(request));
        assertTrue(UserAgentUtils.isWechatMiniProgram(request));
        assertEquals("WechatMiniProgram", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testAlipayApp() {
        request.addHeader("User-Agent", UA_ALIPAY_ANDROID);
        assertTrue(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isAlipay(request));
        assertFalse(UserAgentUtils.isWechat(request));
        assertEquals("Alipay", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testDingTalkApp() {
        request.addHeader("User-Agent", UA_DING_TALK);
        assertTrue(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isDingTalk(request));
        assertEquals("DingTalk", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testRobot() {
        request.addHeader("User-Agent", UA_GOOGLE_BOT);
        assertTrue(UserAgentUtils.isRobot(request));
        assertFalse(UserAgentUtils.isMobile(request));
    }

    @Test
    void testNullOrEmptyRequest() {
        MockHttpServletRequest  request = new MockHttpServletRequest();
        assertEquals("Unknown", UserAgentUtils.getOsName(request));
        assertEquals("Unknown/Unknown", UserAgentUtils.getBrowserName(request));
        assertFalse(UserAgentUtils.isWechat(request));
        assertEquals("Unknown", UserAgentUtils.getDomesticPlatform(request));
    }
    @Test
    void testWeiboApp() {
        request.addHeader("User-Agent", UA_WEIBO);
        assertTrue(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isWeibo(request));
        assertEquals("WeiBo", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testDouYinApp() {
        request.addHeader("User-Agent", UA_DOU_YIN);
        assertTrue(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isDouYin(request));
        assertEquals("DouYin", UserAgentUtils.getDomesticPlatform(request));
    }

    @Test
    void testWxWorkApp() {
        // 1. 正向测试：企业微信 iOS 端
        request.addHeader("User-Agent", UA_WXWORK_IOS);
        assertTrue(UserAgentUtils.isWxWork(request));
        assertTrue(UserAgentUtils.isMobile(request));
        // 企业微信 UA 中通常也包含 MicroMessenger，若 isWechat 也成立可一并断言
        assertTrue(UserAgentUtils.isWechat(request));

        // 2. 反向测试：普通微信不应该被判定为企业微信
        request.removeHeader("User-Agent");
        request.addHeader("User-Agent", UA_WECHAT_IOS);
        assertFalse(UserAgentUtils.isWxWork(request));
    }

    @Test
    void testWxWorkAndroid() {
        // 3. 正向测试：企业微信 Android 端
        request.addHeader("User-Agent", UA_WXWORK_ANDROID);
        assertTrue(UserAgentUtils.isWxWork(request));
        assertTrue(UserAgentUtils.isMobile(request));
    }

    @Test
    @DisplayName("测试 Chrome + Windows 10/11 降级兜底逻辑（用户给出的用例基准）")
    void testChromeWindows10Or11Fallback() {
        request.addHeader(USER_AGENT, UA_CHROME_WINDOWS);

        // 如果项目中包含 getBrowserName 方法，可取消下行注释
        // assertEquals("Chrome/120", UserAgentUtils.getBrowserName(request));
        assertEquals("Windows 10/11", UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 1：请求未携带任何 User-Agent Header，parseUserAgent 返回 null 或无法识别")
    void testNoUserAgentHeader() {
        // 不设置任何 Header
        assertEquals(UNKNOWN, UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 2：User-Agent 无法被解析出有效操作系统（osName 为 Unknown）")
    void testInvalidUserAgent() {
        request.addHeader(USER_AGENT, "Invalid-Agent-String/1.0");
        assertEquals(UNKNOWN, UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 3.1：缺失 Client Hints 高熵信息，命中 Windows NT 10.0 兜底降级逻辑")
    void testWindows10Or11FallbackWithQuestionMarkVersion() {
        // 传统的 Windows 10 UA，无 Client Hints 时版本识别为 ?? 或 Unknown
        request.addHeader(USER_AGENT, UA_CHROME_WINDOWS);

        // 可选：如果包含 Sec-CH-UA-Platform 等 Low-entropy Hints 模拟
        request.addHeader("Sec-CH-UA-Platform", "\"Windows\"");

        assertEquals("Windows 10/11", UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 3.2：osVersion 为 Unknown，UA 属于 Windows NT 但非 10.0（如 Windows 7）")
    void testWindowsNtOtherVersionFallback() {
        request.addHeader(USER_AGENT, UA_WINDOWS_NT6_1);

        // 假设解析器识别 osName 为 "Windows NT"，但 osVersion 为 "Unknown" 或 "??"
        // 命中 WINDOWS_NT.equalsIgnoreCase(osName) -> 返回 "Windows"
        assertEquals("Windows 7", UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 3.3：osVersion 未知，且为非 Windows 操作系统（如 macOS 且缺少高熵版本）")
    void testMacOsWithoutHighEntropyVersionFallback() {
        // 如果 macOS 同样缺失精准版本信息（如被保护隐私隐蔽为 10_15_7 或未知版本）
        request.addHeader(USER_AGENT, UA_MAC);
        String result = UserAgentUtils.getOsNameAndVersion(request);
        assertEquals("Mac OS >=10.15.7", result);
    }

    @Test
    @DisplayName("路径 4.1：携带 Client Hints 高熵 Header，成功解析准确的 Windows 版本（如 Win 11）")
    void testWindowsWithClientHintsHighEntropyVersion() {
        request.addHeader(USER_AGENT, UA_CHROME_WINDOWS);
        request.addHeader("Sec-CH-UA-Platform", "\"Windows\"");
        request.addHeader("Sec-CH-UA-Platform-Version", "\"15.0.0\""); // Windows 11 高熵版本

        assertEquals("Windows 10/11", UserAgentUtils.getOsNameAndVersion(request));
    }

    @Test
    @DisplayName("路径 4.2：成功解析非 Windows 操作系统及版本（如 Android 14）")
    void testAndroidWithVersion() {
        request.addHeader(USER_AGENT, UA_ANDROID);

        assertEquals("Android 14", UserAgentUtils.getOsNameAndVersion(request));
    }
}
