package com.github.hugh.util;

import com.github.binarywang.java.emoji.EmojiConverter;

/**
 * 过滤emoji表情与非emoji表情
 *
 * @author hugh
 * @since 1.0.6
 */
public class EmojiUtils {
    private EmojiUtils() {
    }

    private static final EmojiConverter emojiConverter = EmojiConverter.getInstance();

    /**
     * 检测是否有emoji字符
     *
     * @param source 需要判断的字符串
     * @return boolean {@code true} 存在
     */
    public static boolean isEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!notEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 非emoji表情字符判断
     *
     * @param codePoint code码
     * @return boolean
     */
    private static boolean notEmojiCharacter(char codePoint) {
        return codePoint == 0x0 || codePoint == 0x9 || codePoint == 0xA || codePoint == 0xD || codePoint >= 0x20 && codePoint <= 0xD7FF || codePoint >= 0xE000 && codePoint <= 0xFFFD;
    }

    /**
     * 将字符串中的emoji表情转换成html字符
     *
     * @param value 字符串
     * @return String 表情替换成html后的字符串
     */
    public static String toHtml(String value) {
        return emojiConverter.toHtml(value);
    }

    /**
     * 校验字符串中是否含有emoji表情，如果含有替换成特殊的“口”字符
     *
     * @param value 字符串
     * @return String 替换后的字符串
     */
    private static String replace(String value) {
        StringBuilder buf = new StringBuilder();
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char codePoint = value.charAt(i);
            if (notEmojiCharacter(codePoint)) { // 校验是否包含emoji表情
                buf.append(codePoint);
            } else {
                buf.append(""); // 有moji表情替换成“”口字符
            }
        }
        return buf.toString();
    }

    /**
     * 完整转换emoji表情
     * <ul>
     * <li>注：先将emoji表情转换成html字符串、后再进行校验是否还有未知的emoji表情未转换成功，然后在进行替换</li>
     * </ul>
     *
     * @param value 字符串
     * @return String 完整转换后
     */
    public static String complete(String value) {
        String result = toHtml(value);// 先将字符串转换成HTML字符串
        if (isEmoji(result)) {// 由于个别表情无法完全转换，这里再进行认证
            return replace(result); // 如有还含有emoji表情再进行替换
        }
        return result;
    }
}
