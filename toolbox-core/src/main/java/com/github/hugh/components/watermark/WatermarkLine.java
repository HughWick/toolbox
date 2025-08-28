package com.github.hugh.components.watermark;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 结构化水印内容行，方便传入和绘制。
 * 该类用于定义水印中的每一行文本数据，支持两种类型的内容：
 * 1. 键值对形式 (key: value)，其中值可以选择高亮显示。
 * 2. 完整文本形式，用于标题或不需要键值对格式的独立行。
 *
 * @since 3.0.8
 */
@Setter
@Getter
@NoArgsConstructor
public class WatermarkLine {
    /**
     * 文本的键部分，例如“地点”、“海拔”等。
     * 当 {@code overallText} 不为空时，此字段通常为 {@code null}。
     */
    String key;

    /**
     * 文本的值部分，例如“湖南省湘西花垣县猫儿乡086乡道”、“273.3米”等。
     * 当 {@code overallText} 不为空时，此字段通常为 {@code null}。
     */
    String value;

    /**
     * 指示 {@code value} 是否需要高亮显示（例如改变颜色）。
     * 仅当使用键值对构造函数时有效。
     */
    boolean highlightValue; // 是否高亮显示值

    /**
     * 完整文本，用于没有键值对的行，例如“工程记录”作为标题。
     * 当 {@code key} 和 {@code value} 不为空时，此字段通常为 {@code null}。
     */
    String overallText; // 完整文本，用于没有键值对的行 (如“工程记录”)

    /**
     * 构造函数，用于创建只有一行完整文本的水印内容。
     * 适用于标题行或简单的信息行。
     *
     * @param overallText 完整的水印文本内容。
     */
    public WatermarkLine(String overallText) {
        this.overallText = overallText;
    }

    /**
     * 构造函数，用于创建包含键值对的水印内容行。
     * 值可以选择是否高亮显示。
     *
     * @param key            文本的键部分。
     * @param value          文本的值部分。
     * @param highlightValue 如果值为 {@code true}，则值部分将高亮显示。
     */
    public WatermarkLine(String key, String value, boolean highlightValue) {
        this.key = key;
        this.value = value;
        this.highlightValue = highlightValue;
    }
}
