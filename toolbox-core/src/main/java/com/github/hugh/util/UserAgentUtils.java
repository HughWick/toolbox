package com.github.hugh.util;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * UserAgent 与 请求头解析工具类（基于 Yauaa 实现）
 *
 * @since 3.0.26
 */
public class UserAgentUtils {

    private UserAgentUtils() {
    }

    private static final String USER_AGENT = "User-Agent";
    private static final String UNKNOWN = "Unknown";
    private static final String SLASH = "/"; // 如项目中有 StrPool.SLASH 可直接替换
    // --- 国内常见 APP 的 User-Agent 特征词 ---
    private static final String KEYWORD_WECHAT = "MicroMessenger";   // 微信
    private static final String KEYWORD_WX_WORK = "wxwork";          // 企业微信
    private static final String KEYWORD_MINI_PROGRAM = "miniProgram";// 微信小程序
    private static final String KEYWORD_ALIPAY = "AlipayClient";     // 支付宝
    private static final String KEYWORD_DING_TALK = "DingTalk";       // 钉钉
    private static final String KEYWORD_WEIBO = "Weibo";             // 微博
    private static final String KEYWORD_DOU_YIN = "aweme";            // 抖音 (ByteDance)
    private static final String KEYWORD_QQ = "QQ/";                  // 手机QQ (注意斜杠，区分QQ浏览器MQQBrowser)

    /**
     * Yauaa 解析器单例（性能调优关键）
     * 1. 显式限制 withFields：仅加载需要的字段，大幅降低内存占用和初始化耗时（毫秒级启动）
     * 2. withCache：设置 LRU 缓存，避免高并发下对相同的 UA 重复解析
     */
    private static final UserAgentAnalyzer UAA = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000) // 缓存 10000 条解析结果
            .withFields(
                    UserAgent.AGENT_NAME,
                    UserAgent.AGENT_VERSION,
                    UserAgent.OPERATING_SYSTEM_NAME,
                    UserAgent.OPERATING_SYSTEM_VERSION,
                    UserAgent.DEVICE_CLASS,
                    UserAgent.DEVICE_NAME,
                    UserAgent.LAYOUT_ENGINE_NAME
            )
            .build();

    /**
     * 获取浏览器名称+版本号
     *
     * @param request 请求信息头
     * @return String 浏览器名称+版本号 (如: Chrome/120.0.0.0)
     */
    public static String getBrowserName(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN + SLASH + UNKNOWN;
        }
        String name = agent.getValue(UserAgent.AGENT_NAME);
        String version = agent.getValue(UserAgent.AGENT_VERSION);
        name = isUnknown(name) ? UNKNOWN : name;
        version = isUnknown(version) ? UNKNOWN : version;
        return name + SLASH + version;
    }

    /**
     * 获取操作系统名称
     *
     * @param request 请求信息头
     * @return String 操作系统名称 (如: Windows, Android, iOS, macOS)
     */
    public static String getOsName(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        // 优先尝试读取 HTTP Client Hints (现代 Chrome 默认会自动发送 Sec-CH-UA-Platform: "Windows")
        String platformHeader = request.getHeader("Sec-CH-UA-Platform");
        if (platformHeader != null && !platformHeader.isBlank()) {
            // 去除标头自带的双引号，如 "Windows" -> Windows
            return platformHeader.replace("\"", "").trim();
        }
        // 兜底使用 UserAgent 解析
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String osName = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME);
        if (isUnknown(osName)) {
            return UNKNOWN;
        }
        // 针对 Windows NT 进行人类友好化转换
        if ("Windows NT".equalsIgnoreCase(osName)) {
            return "Windows";
        }
        return osName;
    }

    /**
     * 获取浏览器的版本号字符串
     * <p>
     * 注意：由于废弃了 Bitwalker 的 Version 对象，此处建议直接返回 String 版本号。
     *
     * @param request 请求信息头
     * @return String 版本号 (如: 120.0.0.0)
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String version = agent.getValue(UserAgent.AGENT_VERSION);
        return isUnknown(version) ? UNKNOWN : version;
    }

    /**
     * 获取用户浏览器信息与系统信息原始字符串
     *
     * @param request 请求头
     * @return String User-Agent 字符串
     */
    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader(USER_AGENT);
    }

    /**
     * 获取请求头部信息 Map
     *
     * @param request HTTPServletRequest 请求对象
     * @return 包含所有 HTTP 请求头部信息的 Map 对象
     */
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        if (request == null) {
            return Collections.emptyMap();
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (headerName != null) {
                map.put(headerName, request.getHeader(headerName));
            }
        }
        return map;
    }

    /**
     * 获取完整的操作系统及版本号
     *
     * @return 例如: Windows 10 / Android 13 / iOS 16.5
     */
    public static String getOsNameAndVersion(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String osName = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME);
        String osVersion = agent.getValue(UserAgent.OPERATING_SYSTEM_VERSION);
        if (isUnknown(osName)) {
            return UNKNOWN;
        }
        // 针对 Yauaa 返回 "??" 或老旧 "Windows NT" 的友好转换
        if ("Windows NT".equalsIgnoreCase(osName)) {
            osName = "Windows";
        }
        // 如果 osVersion 为未知或者 "??"，只返回系统名称
        if (isUnknown(osVersion) || "??".equals(osVersion)) {
            return osName;
        }
        return osName + " " + osVersion;
    }

    /**
     * 获取设备类型/客户端类型 (DeviceClass)
     *
     * @return 例如: Desktop(电脑), Phone(手机), Tablet(平板), Robot(爬虫/机器人), TV(电视)
     */
    public static String getDeviceClass(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String deviceClass = agent.getValue(UserAgent.DEVICE_CLASS);
        return isUnknown(deviceClass) ? UNKNOWN : deviceClass;
    }

    /**
     * 判断是否为移动设备终端 (手机或平板)
     */
    public static boolean isMobile(HttpServletRequest request) {
        String deviceClass = getDeviceClass(request);
        return "Phone".equalsIgnoreCase(deviceClass) || "Tablet".equalsIgnoreCase(deviceClass);
    }

    /**
     * 判断是否为爬虫/网络机器人 (Robot / Crawler)
     */
    public static boolean isRobot(HttpServletRequest request) {
        String deviceClass = getDeviceClass(request);
        return "Robot".equalsIgnoreCase(deviceClass) || "Robot Mobile".equalsIgnoreCase(deviceClass);
    }

    /**
     * 获取具体设备品牌/型号名称
     *
     * @return 例如: Apple iPhone, Huawei, Samsung 等
     */
    public static String getDeviceName(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String deviceName = agent.getValue(UserAgent.DEVICE_NAME);
        return isUnknown(deviceName) ? UNKNOWN : deviceName;
    }

    /**
     * 获取浏览器排版/渲染内核引擎
     *
     * @return 例如: Blink, WebKit, Gecko, Trident
     */
    public static String getLayoutEngine(HttpServletRequest request) {
        UserAgent agent = parseUserAgent(request);
        if (agent == null) {
            return UNKNOWN;
        }
        String engine = agent.getValue(UserAgent.LAYOUT_ENGINE_NAME);
        return isUnknown(engine) ? UNKNOWN : engine;
    }

    /**
     * 私有解析方法：增加判空与缓存提取
     */
    private static UserAgent parseUserAgent(HttpServletRequest request) {
        String userAgentStr = getUserAgent(request);
        if (userAgentStr == null || userAgentStr.trim().isEmpty()) {
            return null;
        }
        return UAA.parse(userAgentStr);
    }

    private static boolean isUnknown(String val) {
        return val == null || val.isEmpty() || "Unknown".equalsIgnoreCase(val) || "??".equals(val);
    }

    /**
     * 判断是否在微信环境（含微信普通浏览器和微信小程序）
     */
    public static boolean isWechat(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_WECHAT);
    }

    /**
     * 判断是否在微信小程序环境
     * 注意：部分 Android 机型的微信小程序 UA 可能仅包含 miniProgram 或两者皆有
     */
    public static boolean isWechatMiniProgram(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_MINI_PROGRAM);
    }

    /**
     * 判断是否在企业微信环境
     */
    public static boolean isWxWork(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_WX_WORK);
    }

    /**
     * 判断是否在支付宝环境（含支付宝小程序）
     */
    public static boolean isAlipay(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_ALIPAY);
    }

    /**
     * 判断是否在钉钉环境
     */
    public static boolean isDingTalk(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_DING_TALK);
    }

    /**
     * 判断是否在微博内嵌环境
     */
    public static boolean isWeibo(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_WEIBO);
    }

    /**
     * 判断是否为抖音（字节跳动系）环境
     */
    public static boolean isDouYin(HttpServletRequest request) {
        return containsKeyword(request, KEYWORD_DOU_YIN);
    }

    /**
     * 综合获取国内平台名称（业务路由常用）
     *
     * @return 平台名称，若都不是则返回 Other 或 Unknown
     */
    public static String getDomesticPlatform(HttpServletRequest request) {
        String ua = getUserAgent(request);
        if (ua == null || ua.isEmpty()) return UNKNOWN;
        if (ua.contains(KEYWORD_MINI_PROGRAM)) return "WechatMiniProgram";
        if (ua.contains(KEYWORD_WX_WORK)) return "WxWork";
        if (ua.contains(KEYWORD_WECHAT)) return "Wechat";
        if (ua.contains(KEYWORD_ALIPAY)) return "Alipay";
        if (ua.contains(KEYWORD_DING_TALK)) return "DingTalk";
        if (ua.contains(KEYWORD_DOU_YIN)) return "DouYin";
        if (ua.contains(KEYWORD_WEIBO)) return "WeiBo";
        if (ua.contains(KEYWORD_QQ)) return "QQ";
        return "Other";
    }

    /**
     * 快速校验 UA 中是否包含指定关键字（忽略大小写，为了性能直接走原始 String api）
     */
    private static boolean containsKeyword(HttpServletRequest request, String keyword) {
        String ua = getUserAgent(request);
        return ua != null && ua.contains(keyword);
    }
}
