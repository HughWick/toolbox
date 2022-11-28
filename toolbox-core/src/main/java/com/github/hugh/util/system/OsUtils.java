package com.github.hugh.util.system;

/**
 * 系统工具
 *
 * @author hugh
 * @since 1.1.0
 */
public class OsUtils {
    private OsUtils() {

    }

    /**
     * 系统名称：windows、linx
     */
    private static final String OS_NAME = "os.name";

    /**
     * 系统架构
     */
    private static final String OS_ARCH = "os.arch";

    /**
     * 校验当前系统是否为windows系统
     *
     * @return boolean {@code true} windows
     * @since 1.1.0
     */
    public static boolean isWindows() {
        return getOsName().toLowerCase().startsWith("win");
    }

    /**
     * 当前系统为linux
     *
     * @return boolean {@code true} linux
     * @since 2.1.11
     */
    public static boolean isLinux() {
        return getOsName().toLowerCase().contains("linux");
    }

    /**
     * 系统架构
     *
     * @return boolean {@code true}
     * @since 2.4.3
     */
    public static boolean is64() {
        final String property = getOsArch();
        return property.equals("amd64") || property.equals("x86_64");
    }

    /**
     * 系统架构是arm 64
     *
     * @return boolean
     * @since 2.4.3
     */
    public static boolean isAarch64() {
        return getOsArch().equals("aarch64");
    }

    /**
     * 获取当前系统架构
     *
     * @return String
     */
    private static String getOsArch() {
        return System.getProperty(OS_ARCH);
    }

    /**
     * 获取系统名称，windows，linux
     *
     * @return String
     */
    private static String getOsName() {
        return System.getProperty(OS_NAME);
    }
}
