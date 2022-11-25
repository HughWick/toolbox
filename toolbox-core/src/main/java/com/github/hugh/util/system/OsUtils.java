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
        String os = System.getProperty(OS_NAME);
        return os.toLowerCase().startsWith("win");
    }

    /**
     * 当前系统为linux
     *
     * @return boolean {@code true} linux
     * @since 2.1.11
     */
    public static boolean isLinux() {
        return System.getProperty(OS_NAME).toLowerCase().contains("linux");
    }

    /**
     * 系统架构
     *
     * @return boolean {@code true}
     */
    public static boolean is64() {
        final String property = System.getProperty(OS_ARCH);
        return property.equals("amd64") || property.equals("x86_64");
    }

    /**
     * 系统架构是arm 64
     *
     * @return boolean
     */
    public static boolean isAarch64() {
        final String property = System.getProperty(OS_ARCH);
        return property.equals("aarch64");
    }

}
