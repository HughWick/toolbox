package com.github.hugh.components.watermark;

import com.github.hugh.util.font.FontUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 复杂水印配置的建造者（Builder）类。
 * 该类使用 Lombok 注解来自动生成 setter 和 getter 方法，并支持链式调用，
 * 使得构建复杂水印所需的所有参数变得清晰和便捷。
 * 用户可以通过链式调用 {@code setXxx()} 方法逐步设置水印的各项属性，
 * 然后将构建好的此对象传递给水印生成方法。
 *
 * @since 3.0.8
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
     * 水印与目标图片上边缘的距离（单位：像素）。
     * <p>
     * 该值用于在基础定位（如 {@code Positions.TOP_LEFT}）计算出的坐标基础上，
     * 进一步向下偏移水印，从而在水印和图片顶部之间创建出指定的垂直间距。
     * 默认为 0，表示不设置上边距。
     */
    private int marginTop = 0;

    /**
     * 水印与目标图片下边缘的距离（单位：像素）。
     * <p>
     * 该值用于在基础定位（如 {@code Positions.BOTTOM_LEFT}）计算出的坐标基础上，
     * 进一步向上偏移水印，从而在水印和图片底部之间创建出指定的垂直间距。
     * 默认为 0，表示不设置下边距。
     */
    private int marginBottom = 0;

    /**
     * 水印与目标图片左边缘的距离（单位：像素）。
     * <p>
     * 该值用于在基础定位（如 {@code Positions.TOP_LEFT}）计算出的坐标基础上，
     * 进一步向右偏移水印，从而在水印和图片左侧之间创建出指定的水平间距。
     * 默认为 0，表示不设置左边距。
     */
    private int marginLeft = 0;

    /**
     * 水印与目标图片右边缘的距离（单位：像素）。
     * <p>
     * 该值用于在基础定位（如 {@code Positions.TOP_RIGHT}）计算出的坐标基础上，
     * 进一步向左偏移水印，从而在水印和图片右侧之间创建出指定的水平间距。
     * 默认为 0，表示不设置右边距。
     */
    private int marginRight = 0;

    /**
     * 无参构造函数。
     * 允许外部通过 {@code new ComplexWatermarkBuilder()} 的方式创建实例，
     * 然后通过链式 {@code setXxx()} 方法设置各项参数。
     */
    public ComplexWatermarkBuilder() {
    }

    // --- 新增的重载方法，允许直接传入文件路径、File 对象或 InputStream ---

    /**
     * 设置要添加水印的目标图片，通过图片文件的完整路径。
     * 此方法内部会调用 {@link ImageWaterMark#loadImage(String)} 来加载图片。
     *
     * @param imagePath 图片文件的完整路径。
     * @return 当前建造者实例，支持链式调用。
     * @throws IOException 如果图片文件不存在或无法读取。
     */
    public ComplexWatermarkBuilder setTargetImage(String imagePath) throws IOException {
        this.targetImage = ImageWaterMark.loadImage(imagePath);
        return this;
    }

    /**
     * 设置要添加水印的目标图片，通过 {@code File} 对象。
     * 此方法内部会调用 {@link ImageWaterMark#loadImage(File)} 来加载图片。
     *
     * @param imageFile 图片文件对象。
     * @return 当前建造者实例，支持链式调用。
     * @throws IOException 如果文件不存在、无法读取、或者文件内容不是有效的图片格式。
     */
    public ComplexWatermarkBuilder setTargetImage(File imageFile) throws IOException {
        this.targetImage = ImageWaterMark.loadImage(imageFile);
        return this;
    }

    /**
     * 设置要添加水印的目标图片，通过 {@code InputStream} 流。
     * 此方法内部会调用 {@link ImageWaterMark#loadImage(InputStream)} 来加载图片。
     *
     * @param inputStream 包含图片数据的输入流。
     * @return 当前建造者实例，支持链式调用。
     * @throws IOException 如果读取流时发生I/O错误，或者流中的数据不是有效的图片格式。
     */
    public ComplexWatermarkBuilder setTargetImage(InputStream inputStream) throws IOException {
        this.targetImage = ImageWaterMark.loadImage(inputStream);
        return this;
    }
}
