package com.github.hugh.components.watermark;

import com.github.hugh.util.font.FontUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 复杂水印配置的建造者（Builder）类。
 * 该类使用 Lombok 注解来自动生成 setter 和 getter 方法，并支持链式调用，
 * 使得构建复杂水印所需的所有参数变得清晰和便捷。
 * 用户可以通过链式调用 {@code setXxx()} 方法逐步设置水印的各项属性，
 * 然后将构建好的此对象传递给水印生成方法。
 */
@Setter // 为所有字段自动生成公共的 setter 方法 (例如: setTargetImage(...))
@Getter // 为所有字段自动生成公共的 getter 方法 (例如: getTargetImage())
@Accessors(chain = true) // 配置 setter 方法返回当前对象（this），从而支持链式调用 (例如: .setTargetImage(...).setWatermarkContent(...))
public class ComplexWatermarkBuilder {

    /**
     * 要添加水印的目标图片。
     */
    private BufferedImage targetImage;

    /**
     * 水印的文本内容列表。列表的第一个元素通常作为水印的标题。
     * 每个 {@link WatermarkLine} 对象代表水印中的一行文本，可以是键值对或完整文本。
     */
    private List<WatermarkLine> watermarkContent;
    /**
     * 水印标题文本使用的字体。
     */
    private Font titleFont = FontUtils.load(FontUtils.DEFAULT_FONT_PATH_BOLD, 24, Font.BOLD);
    /**
     * 水印内容（除标题外）文本使用的字体。
     */
    private Font contentFont = FontUtils.load(FontUtils.DEFAULT_FONT_PATH, 18, Font.PLAIN);
    /**
     * 水印的透明度，取值范围通常为 0.0f（完全透明）到 1.0f（完全不透明）。
     */
    private float opacity = 0.8f;

    /**
     * 水印在目标图片上的放置位置，使用 {@link Positions} 枚举定义。
     * 例如：{@code Positions.BOTTOM_LEFT} 表示左下角。
     */
    private Positions position = Positions.BOTTOM_LEFT;

    /**
     * 生成水印后输出图片的文件路径。
     */
    private String outPath;

    /**
     * 显示在水印内容区域底部的公司信息字符串。如果为空或null，则不显示。
     */
    private String companyInfo;

    /**
     * 无参构造函数。
     * 允许外部通过 {@code new ComplexWatermarkBuilder()} 的方式创建实例，
     * 然后通过链式 {@code setXxx()} 方法设置各项参数。
     */
    public ComplexWatermarkBuilder() {
        // 可以在此设置字段的默认值，如果它们没有在字段声明时直接初始化。
        // 例如：
        // this.opacity = 1.0f;
        // this.position = Positions.BOTTOM_RIGHT;
        // this.watermarkContent = new ArrayList<>();
    }
}
