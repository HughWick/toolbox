package com.github.hugh.util.font;

import java.awt.*;
import java.io.IOException;

public class FontUtils {

    public static final String DEFAULT_FONT_PATH = "fonts/SourceHanSansCN-Regular.otf";

    public static final String DEFAULT_FONT_PATH_BOLD = "fonts/SourceHanSansCN-Bold.otf";

    /**
     * 从类路径加载字体文件，如果失败则返回一个备用默认字体。
     *
     * @param fontPath  字体文件在类路径中的相对路径，例如 "fonts/MyFont.otf"。
     * @param fontSize  字体大小。
     * @param fontStyle 字体样式（如 Font.PLAIN, Font.BOLD）。
     * @return 加载的 Font 对象，如果加载失败则返回一个通用的 SansSerif 字体。
     */
    public static Font load(String fontPath, int fontSize, int fontStyle) {
        try {
            java.net.URL fontUrl = FontUtils.class.getClassLoader().getResource(fontPath);
            if (fontUrl == null) {
                System.err.println("警告：未找到默认字体资源。请检查路径: " + fontPath + "。将使用备用字体。");
                return new Font("SansSerif", fontStyle, fontSize); // 备用字体
            }
            java.io.InputStream fontStream = FontUtils.class.getClassLoader().getResourceAsStream(fontPath);
            if (fontStream == null) {
                System.err.println("警告：无法获取默认字体输入流，尽管URL已找到。路径: " + fontPath + "。将使用备用字体。");
                return new Font("SansSerif", fontStyle, fontSize); // 备用字体
            }
            return Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontStyle, fontSize);
        } catch (FontFormatException | IOException e) {
            System.err.println("警告：加载默认字体时发生错误: " + fontPath + ". " + e.getMessage() + "。将使用备用字体。");
            return new Font("SansSerif", fontStyle, fontSize); // 备用字体
        }
    }
}
