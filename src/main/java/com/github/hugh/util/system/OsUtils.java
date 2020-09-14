package com.github.hugh.util.system;

/**
 * 系统工具
 *
 * @author hugh
 * @since 1.1.0
 */
public class OsUtils {

    /**
     * 校验当前系统是否为windows系统
     *
     * @return boolean {@code true} windows
     * @since 1.1.0
     */
    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }
}
