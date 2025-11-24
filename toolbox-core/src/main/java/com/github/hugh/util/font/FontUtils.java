package com.github.hugh.util.font;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FontUtils {
    private FontUtils() {
    }

    public static final String DEFAULT_FONT_PATH = "fonts/SourceHanSansCN-Regular.otf";

    public static final String DEFAULT_FONT_PATH_BOLD = "fonts/SourceHanSansCN-Bold.otf";

    /**
     * 从类路径（Classpath）加载指定的字体文件。
     * <p>
     * 如果加载失败（文件不存在、格式错误或 IO 异常），该方法会记录错误日志
     * 并返回一个系统默认的 "SansSerif" 字体，以确保程序不会因为字体缺失而崩溃。
     * </p>
     *
     * @param fontPath  字体文件在类路径中的相对路径（例如 "fonts/MyFont.otf"）
     * @param fontSize  字体大小（例如 12, 24）
     * @param fontStyle 字体样式（通常使用 {@link Font#PLAIN}, {@link Font#BOLD} 或 {@link Font#ITALIC}）
     * @return 加载成功的 {@link Font} 对象；如果加载失败，则返回备用的 SansSerif 字体
     */
    public static Font load(String fontPath, int fontSize, int fontStyle) {
        try (InputStream fontStream = FontUtils.class.getClassLoader().getResourceAsStream(fontPath)) {
            if (fontStream == null) {
                log.warn(" 未找到默认字体资源，请检查路径:{}。将使用备用字体。", fontPath);
                return new Font("SansSerif", fontStyle, fontSize); // 备用字体
            }
            // 创建字体并设置样式和大小
            return Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(fontStyle, (float) fontSize);
        } catch (FontFormatException | IOException e) {
            log.error("加载默认字体时发生错误:{}。错误信息:{}。将使用备用字体。", fontPath, e.getMessage(), e);
            return new Font("SansSerif", fontStyle, fontSize); // 备用字体
        }
    }
}
