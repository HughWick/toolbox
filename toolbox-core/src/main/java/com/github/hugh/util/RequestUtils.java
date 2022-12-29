package com.github.hugh.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

import javax.servlet.http.HttpServletRequest;

/**
 * 设备工具类
 *
 * @author hugh
 * @since 1.2.8
 */
public class RequestUtils {

    /**
     * 浏览器用户代理常量
     */
    private static final String USER_AGENT = "user-agent";

    /**
     * 根据{@link HttpServletRequest}获取请求头内的设备类型
     * <ul>
     * <li>支持:微信浏览器,安卓浏览器,IOS浏览器,其他都为PC端</li>
     * </ul>
     *
     * @param request 请求头
     * @return String 设备类型
     */
    public static String getSystemType(HttpServletRequest request) {
        String userAgent = getUserAgent(request).toLowerCase();
        if (userAgent.contains("micromessenger")) { // 微信
            return "wx";
        } else if (userAgent.contains("android")) { // 安卓
            return "android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) { // 苹果
            return "ios";
        } else { // 电脑
            return "pc";
        }
    }

    /**
     * 判断请求头是否为微信内置浏览器
     *
     * @param request 请求信息
     * @return boolean {@code true} 是微信浏览器
     */
    public static boolean isWeChat(HttpServletRequest request) {
        return "wx".equals(getSystemType(request));
    }

    /**
     * 判断请求头是否为安卓端
     *
     * @param request 请求信息
     * @return boolean {@code true} 是
     */
    public static boolean isAndroid(HttpServletRequest request) {
        return "android".equals(getSystemType(request));
    }

    /**
     * 判断请求头是否为IOS系统请求
     * <ul>
     * <li>注:iphone,ipad,ipod都为IOS客户端</li>
     * </ul>
     *
     * @param request 请求头信息
     * @return boolean {@code true} 是IOS系统
     */
    public static boolean isIos(HttpServletRequest request) {
        return "ios".equals(getSystemType(request));
    }

    /**
     * 判断请求头是否为PC端浏览器请求
     * <ul>
     * <li>注:该方法内置先通过{@link RequestUtils#getSystemType(HttpServletRequest)} 获取到各个客户端的浏览器或系统类型后,如若没有匹配到对应的类型时,则都判定为PC端</li>
     * </ul>
     *
     * @param request 请求信息头
     * @return boolean {@code true} 是
     */
    public static boolean isPc(HttpServletRequest request) {
        return "pc".equals(getSystemType(request));
    }

    /**
     * 获取浏览器名称
     *
     * @param request 请求信息头
     * @return String 浏览器名称+版本号
     * @since 2.3.6
     */
    public static String getBrowserName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        return browser.getName() + "/" + browser.getVersion(userAgent);
    }

    /**
     * 获取操作系统
     *
     * @param request 请求信息头
     * @return String 操作系统名称
     * @since 2.3.6
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        OperatingSystem os = ua.getOperatingSystem();
        return os.getName();
    }

    /**
     * 获取浏览器的版本号
     *
     * @param request 请求信息头
     * @return Version 版本号
     * @since 2.4.9
     */
    public static Version getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        UserAgent ua = UserAgent.parseUserAgentString(userAgent);
        Browser browser = ua.getBrowser();
        return browser.getVersion(userAgent);
    }

    /**
     * 获取用户浏览器信息与系统信息
     *
     * @param request 请求头
     * @return String
     * @since 2.4.9
     */
    public static String getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader(USER_AGENT);
    }
}
