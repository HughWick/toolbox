package com.github.hugh.components.watermark;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * {@code ImageWaterMark} 类是一个图片水印工具类，提供向图片添加复杂结构化水印的功能。
 * 它封装了水印尺寸的计算、图形元素的绘制、字体加载以及最终水印应用的整个流程。
 *
 * <p>该类支持通过 {@link ComplexWatermarkBuilder} 对象灵活配置水印的各项属性，
 * 包括水印内容、字体、透明度、位置以及公司信息等。
 *
 * <p>为了提供更好的用户体验，该类提供了默认字体加载机制，当未指定字体时，
 * 会尝试从类路径中的指定位置加载默认字体，如果加载失败，则会回退到系统通用的
 * "SansSerif" 字体，确保程序在不同环境下（如 Windows 和 Linux）的兼容性和稳定性。</p>
 *
 * @author hugh
 * @since 3.0.8
 */
public class ImageWaterMark {

    // --- 常量定义 ---
    // 标题蓝色背景颜色：RGB (0, 102, 204)，透明度 200 (out of 255)
    private static final Color TITLE_BACKGROUND_COLOR = new Color(0, 102, 204, 200);
    // 内容白色背景颜色：RGB (255, 255, 255)，透明度 220
    private static final Color CONTENT_BACKGROUND_COLOR = new Color(255, 255, 255, 220);
    // 左上角小圆点颜色：RGB (255, 204, 0)，透明度 255
    private static final Color ICON_COLOR = new Color(255, 204, 0, 255);
    // 标题文本颜色：白色
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;
    // 内容文本颜色：黑色
    private static final Color CONTENT_TEXT_COLOR = Color.BLACK;
    // 高亮文本颜色：红色
    private static final Color HIGHLIGHT_TEXT_COLOR = Color.RED;

    // 整个水印UI的左右内边距，也用于内容区域的上下内边距
    private static final int PADDING = 20;
    // 标题文本到蓝色背景上下边缘的距离，可调整此值来控制蓝色区域的高度
    private static final int TITLE_VERTICAL_PADDING = 10;
    // 内容行之间的垂直间距
    private static final int LINE_SPACING = 10;
    // 左上角圆点半径
    private static final int ICON_RADIUS = 5;
    // 键值对之间（key 和 value 之间）的水平间距
    private static final int KEY_VAL_SPACING = 15;
    // 圆角弧度，用于水印背景的圆角效果
    private static final int ARC_SIZE = 20;

    /**
     * 封装水印各个部分的计算尺寸，方便在方法间传递。
     * 此内部类用于存储在绘制水印之前计算好的所有尺寸和度量信息，
     * 包括标题、内容、公司信息区域的宽度、高度、起始位置等，
     * 以及整体水印图像的最终尺寸，以便于在不同的绘制方法中共享这些计算结果。
     */
    private static class WatermarkLayout {
        /**
         * 水印标题文本的显示宽度（像素）。
         */
        int titleWidth;
        /**
         * 水印标题文本的高度（像素），基于字体度量。
         */
        int titleHeight;
        /**
         * 水印标题背景区域的总高度（像素），包括标题文本高度和垂直内边距。
         */
        int titleBgHeight;
        /**
         * 所有内容行中键（key）部分的最大宽度（像素）。
         * 用于对齐键值对中的值（value）。
         */
        int maxKeyWidth;
        /**
         * 所有内容行中值（value）部分的最大宽度（像素）。
         */
        int maxValWidth;
        /**
         * 内容区域中单行文本的最大宽度（像素），可以是键值对的组合宽度或独立文本行的宽度。
         */
        int maxContentLineWidth;
        /**
         * 水印内容文本的行数（不包括标题行和公司信息行）。
         */
        int contentTextLineCount;
        /**
         * 所有内容文本行（不含公司信息）的总高度（像素），不包括行与行之间的额外内边距。
         */
        int contentLinesHeightExcludingPadding;
        /**
         * 公司信息文本的高度（像素）。
         */
        int companyInfoHeight;
        /**
         * 公司信息文本的宽度（像素）。
         */
        int companyInfoWidth;
        /**
         * 所有内容文本行和公司信息文本的总高度（像素），包括它们之间的行间距，但不包括最上下的内边距。
         */
        int totalContentAndCompanyTextHeight;
        /**
         * 白色内容背景区域的总高度（像素），包括所有内容文本、公司信息以及其上下的内边距。
         */
        int contentTotalHeight;
        /**
         * 整个水印图像的最终总宽度（像素）。
         */
        int watermarkWidth;
        /**
         * 整个水印图像的最终总高度（像素）。
         */
        int watermarkHeight;
        /**
         * 标题字体对应的 {@code FontMetrics} 对象，用于计算标题文本的尺寸。
         */
        FontMetrics titleFontMetrics;
        /**
         * 内容字体对应的 {@code FontMetrics} 对象，用于计算内容文本和公司信息文本的尺寸。
         */
        FontMetrics contentFontMetrics;

        /**
         * 构造函数用于初始化水印布局对象，并传入标题和内容字体的 {@code FontMetrics}。
         * * @param titleFm 标题字体的 {@code FontMetrics} 对象。
         * @param contentFm 内容字体的 {@code FontMetrics} 对象。
         */
        public WatermarkLayout(FontMetrics titleFm, FontMetrics contentFm) {
            this.titleFontMetrics = titleFm;
            this.contentFontMetrics = contentFm;
        }
    }

    /**
     * 从文件系统加载 {@code BufferedImage} 图片。
     * 此方法是一个通用的图片加载辅助方法，适用于任何需要读取本地图片文件的场景。
     * 它通过 Java 的 ImageIO 库高效地将图片文件读取为 {@code BufferedImage} 对象。
     *
     * @param imagePath 图片文件的完整路径（例如："C:/images/input.jpg" 或 "/home/user/img.png"）。
     * @return 加载成功的 {@code BufferedImage} 对象。
     * @throws IOException 如果指定的图片文件不存在、无法访问、或者文件内容不是有效的图片格式，则抛出此异常。
     */
    public static BufferedImage loadImage(String imagePath) throws IOException {
        // 将路径字符串转换为 File 对象，并调用loadImage(File file)重载方法。
        return loadImage(new File(imagePath));
    }

    /**
     * 从 {@code File} 对象加载 {@code BufferedImage} 图片。
     * 此方法适用于已知图片文件对象的场景。
     *
     * @param imageFile 图片文件对象。
     * @return 加载成功的 {@code BufferedImage} 对象。
     * @throws IOException 如果文件不存在、无法读取、或者文件内容不是有效的图片格式，则抛出此异常。
     */
    public static BufferedImage loadImage(File imageFile) throws IOException {
        // 使用 Java 标准库的 ImageIO.read 方法直接从文件加载图片。
        return ImageIO.read(imageFile);
    }

    /**
     * 从 {@code InputStream} 流加载 {@code BufferedImage} 图片。
     * 此方法适用于从网络、数据库或内存中读取图片流的场景。
     *
     * @param inputStream 包含图片数据的输入流。
     * @return 加载成功的 {@code BufferedImage} 对象。
     * @throws IOException 如果读取流时发生I/O错误，或者流中的数据不是有效的图片格式，则抛出此异常。
     */
    public static BufferedImage loadImage(InputStream inputStream) throws IOException {
        // 使用 Java 标准库的 ImageIO.read 方法直接从输入流加载图片。
        return ImageIO.read(inputStream);
    }

    /**
     * 为目标图片添加复杂的结构化水印。
     * 该方法封装了水印尺寸计算、图形绘制以及最终应用水印的整个流程。
     *
     * @param complexWatermarkBuilder 包含所有水印配置信息的建造者对象。
     * @throws IOException 如果在处理图片或字体时发生I/O错误。
     */
    public static void addComplex(ComplexWatermarkBuilder complexWatermarkBuilder) throws IOException {
        // 1. 获取必要的字体度量信息
        BufferedImage tempGfxImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dTemp = tempGfxImage.createGraphics();
        g2dTemp.setFont(complexWatermarkBuilder.getTitleFont());
        FontMetrics titleFm = g2dTemp.getFontMetrics();
        g2dTemp.setFont(complexWatermarkBuilder.getContentFont());
        FontMetrics contentFm = g2dTemp.getFontMetrics();
        g2dTemp.dispose(); // 立即释放临时Graphics2D资源
        // 2. 计算水印的布局尺寸
        WatermarkLayout layout = calculateWatermarkLayout(
                complexWatermarkBuilder.getWatermarkContent(),
                complexWatermarkBuilder.getCompanyInfo(),
                titleFm,
                contentFm
        );
        // 3. 创建水印图像并获取其Graphics2D对象
        BufferedImage complexWatermarkImage = new BufferedImage(layout.watermarkWidth, layout.watermarkHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = complexWatermarkImage.createGraphics();
        configureGraphics(g2d); // 配置绘图质量
        try {
            // 4. 绘制水印背景和基本元素
            drawWatermarkBackground(g2d, layout.watermarkWidth, layout.watermarkHeight, layout.titleBgHeight, layout.contentTotalHeight);
            // 5. 绘制左上角小圆点
            drawIcon(g2d, layout.titleBgHeight);
            // 6. 绘制标题
            drawTitle(g2d, complexWatermarkBuilder.getTitleFont(), layout.titleWidth, layout.watermarkWidth, layout.titleBgHeight, layout.titleFontMetrics, complexWatermarkBuilder.getWatermarkContent().get(0).overallText);
            // 7. 绘制内容行
            drawContentLines(g2d, complexWatermarkBuilder.getWatermarkContent(), complexWatermarkBuilder.getContentFont(), layout.titleBgHeight, layout.maxKeyWidth, layout.contentFontMetrics);
            // 8. 绘制公司信息行
            drawCompanyInfo(g2d, complexWatermarkBuilder.getCompanyInfo(), complexWatermarkBuilder.getContentFont(), layout.titleBgHeight, layout.contentTextLineCount, layout.contentFontMetrics);
        } finally {
            g2d.dispose(); // 确保Graphics2D资源被释放
        }
        // 9. 将生成的水印应用到目标图片并保存
        applyAndSaveWatermark(
                complexWatermarkBuilder.getTargetImage(),
                complexWatermarkBuilder.getPosition(),
                complexWatermarkImage,
                complexWatermarkBuilder.getOpacity(),
                complexWatermarkBuilder.getOutPath()
        );
    }

    /**
     * 计算水印布局的各个尺寸。
     * 此方法负责根据传入的水印内容、公司信息以及字体度量，精确计算出水印图像的整体宽度、高度，
     * 以及内部各个组成部分（如标题区域、内容区域、公司信息区域）的尺寸和间距。
     * 计算结果封装在 {@link WatermarkLayout} 对象中返回，供后续的绘制方法使用。
     *
     * @param watermarkContent 包含水印所有文本内容（标题、键值对行）的列表。
     *                         列表的第一个元素期望是水印的标题行。
     * @param companyInfo      公司信息字符串，如果为 {@code null} 或空字符串，则不绘制公司信息行。
     * @param titleFm          标题字体 (FontMetrics) 对象，用于获取标题文本的宽度和高度信息。
     * @param contentFm        内容字体 (FontMetrics) 对象，用于获取内容文本（键值对、公司信息）的宽度和高度信息。
     * @return 包含所有计算尺寸的 {@link WatermarkLayout} 对象。
     */
    private static WatermarkLayout calculateWatermarkLayout(java.util.List<WatermarkLine> watermarkContent, String companyInfo,
                                                            FontMetrics titleFm, FontMetrics contentFm) {
        // 初始化 WatermarkLayout 对象，并将 FontMetrics 传递给它，以便后续访问。
        WatermarkLayout layout = new WatermarkLayout(titleFm, contentFm);
        // 获取并计算标题文本的尺寸。
        // 修正点：titleText 变量不需要作为 WatermarkLayout 的属性，直接从 watermarkContent 获取
        String titleTextContent = watermarkContent.get(0).overallText; // 获取标题文本。假设列表的第一个元素是标题。
        layout.titleWidth = titleFm.stringWidth(titleTextContent); // 计算标题文本的显示宽度。
        layout.titleHeight = titleFm.getHeight(); // 获取标题文本的实际高度。
        // 计算标题背景区域的高度，包括文本高度和上下内边距。
        layout.titleBgHeight = layout.titleHeight + TITLE_VERTICAL_PADDING * 2;
        // 初始化内容区域的相关计数和尺寸。
        layout.contentTextLineCount = watermarkContent.size() - 1; // 内容行数量（排除标题行）。
        layout.contentLinesHeightExcludingPadding = 0; // 内容文本行（不含公司信息）的总高度，不包括额外的上下内边距。
        layout.maxKeyWidth = 0; // 所有内容行中键（key）部分的最大宽度。
        layout.maxValWidth = 0; // 所有内容行中值（value）部分的最大宽度。
        // 遍历内容行（从第二个元素开始），计算其尺寸。
        if (layout.contentTextLineCount > 0) {
            for (int i = 1; i < watermarkContent.size(); i++) {
                WatermarkLine line = watermarkContent.get(i);
                if (line.key != null) {
                    // 更新最大键宽度，包括冒号和空格的宽度。
                    layout.maxKeyWidth = Math.max(layout.maxKeyWidth, contentFm.stringWidth(line.key + ": "));
                    // 更新最大值宽度。
                    layout.maxValWidth = Math.max(layout.maxValWidth, contentFm.stringWidth(line.value));
                }
                // 累加内容文本行的高度。
                layout.contentLinesHeightExcludingPadding += contentFm.getHeight();
            }
            // 如果有多行内容，累加行之间的间距。
            if (layout.contentTextLineCount > 1) {
                layout.contentLinesHeightExcludingPadding += (layout.contentTextLineCount - 1) * LINE_SPACING;
            }
        }
        // 计算内容区域的最大行宽度（键值对的组合宽度）。
        layout.maxContentLineWidth = 0;
        if (layout.contentTextLineCount > 0) {
            layout.maxContentLineWidth = layout.maxKeyWidth + KEY_VAL_SPACING + layout.maxValWidth;
        }
        // 初始化公司信息行的尺寸，并累加到总内容高度。
        layout.companyInfoHeight = 0;
        layout.companyInfoWidth = 0;
        // 初始化总内容文本和公司信息的高度，从已计算的内容行高度开始。
        layout.totalContentAndCompanyTextHeight = layout.contentLinesHeightExcludingPadding;
        // 如果公司信息不为空，则计算其尺寸并更新总高度和最大行宽度。
        if (companyInfo != null && !companyInfo.isEmpty()) {
            layout.companyInfoHeight = contentFm.getHeight(); // 公司信息文本的高度。
            layout.companyInfoWidth = contentFm.stringWidth(companyInfo); // 公司信息文本的宽度。

            // 如果之前有内容行，公司信息行与上方内容之间需要一个间距。
            if (layout.contentTextLineCount > 0) {
                layout.totalContentAndCompanyTextHeight += LINE_SPACING;
            }
            layout.totalContentAndCompanyTextHeight += layout.companyInfoHeight; // 累加公司信息行的高度。
            // 公司信息宽度也可能影响整个内容区域的最大行宽度。
            layout.maxContentLineWidth = Math.max(layout.maxContentLineWidth, layout.companyInfoWidth);
        }
        // 计算最终的白色内容区域的总高度（包括所有内容文本、公司信息和上下内边距）。
        layout.contentTotalHeight = 0;
        if (layout.totalContentAndCompanyTextHeight > 0) {
            layout.contentTotalHeight = layout.totalContentAndCompanyTextHeight + PADDING * 2;
        }
        // 计算水印图像的最终总宽度和总高度。
        // 水印宽度取决于标题宽度和最大内容行宽度的较大者，再加上左右内边距。
        layout.watermarkWidth = Math.max(layout.titleWidth, layout.maxContentLineWidth) + PADDING * 2;
        // 水印总高度是标题背景高度与内容区域总高度之和。
        layout.watermarkHeight = layout.titleBgHeight + layout.contentTotalHeight;
        return layout; // 返回包含所有计算尺寸的布局对象。
    }

    /**
     * 配置 Graphics2D 对象的渲染提示，以获得高质量绘图。
     *
     * @param g2d 要配置的 Graphics2D 对象。
     */
    private static void configureGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    /**
     * 绘制水印的背景区域（蓝色标题背景和白色内容背景）。
     *
     * @param g2d                绘图上下文。
     * @param watermarkWidth     水印总宽度。
     * @param watermarkHeight    水印总高度。
     * @param titleBgHeight      标题背景高度。
     * @param contentTotalHeight 内容区域总高度。
     */
    private static void drawWatermarkBackground(Graphics2D g2d, int watermarkWidth, int watermarkHeight,
                                                int titleBgHeight, int contentTotalHeight) {
        // 1. 绘制整个水印的底层蓝色圆角矩形（作为基础层和上部圆角）。
        g2d.setColor(TITLE_BACKGROUND_COLOR); //
        g2d.fillRoundRect(0, 0, watermarkWidth, watermarkHeight, ARC_SIZE, ARC_SIZE); //
        // 2. 在蓝色背景之上绘制白色内容区域。
        if (contentTotalHeight > 0) { //
            g2d.setColor(CONTENT_BACKGROUND_COLOR); //
            // 绘制白色矩形，从标题背景下方开始，延伸到整个水印的底部，
            // 但其顶部是平直的，底部保持圆角。
            // 这通过绘制一个普通的矩形覆盖顶部，然后用 fillRoundRect 覆盖整个内容区域来实现。
            // 绘制一个覆盖整个内容区域的白色平直顶部矩形，一直到水印的底部
            // 这样可以确保标题背景下方是完全白色的，并且覆盖了蓝色背景的交界处。
            g2d.fillRect(0, titleBgHeight, watermarkWidth, watermarkHeight - titleBgHeight);
            // 然后再在这个白色矩形之上，绘制一个底部带有圆角的白色矩形。
            // 由于 ARC_SIZE 的存在，fillRoundRect 会使顶部和底部都有圆角。
            // 为了消除顶部圆角，我们刚才已经用 fillRect 覆盖了顶部。
            // 这个 fillRoundRect 会确保底部有正确的圆角。
            g2d.fillRoundRect(0, titleBgHeight, watermarkWidth, contentTotalHeight, ARC_SIZE, ARC_SIZE);
        }
    }

    /**
     * 绘制左上角的黄色小圆点图标。
     *
     * @param g2d           绘图上下文。
     * @param titleBgHeight 标题背景高度，用于计算圆点垂直居中位置。
     */
    private static void drawIcon(Graphics2D g2d, int titleBgHeight) {
        g2d.setColor(ICON_COLOR);
        int iconY = (titleBgHeight / 2) - ICON_RADIUS;
        g2d.fillOval(PADDING / 2, iconY, ICON_RADIUS * 2, ICON_RADIUS * 2);
    }

    /**
     * 绘制水印的标题文本。
     *
     * @param g2d            绘图上下文。
     * @param titleFont      标题字体。
     * @param titleWidth     标题文本宽度。
     * @param watermarkWidth 水印总宽度。
     * @param titleBgHeight  标题背景高度。
     * @param titleFm        标题字体的 FontMetrics。
     * @param titleText      标题文本内容。
     */
    private static void drawTitle(Graphics2D g2d, Font titleFont, int titleWidth, int watermarkWidth,
                                  int titleBgHeight, FontMetrics titleFm, String titleText) {
        g2d.setFont(titleFont);
        g2d.setColor(DEFAULT_TEXT_COLOR);
        int titleTextX = (watermarkWidth / 2) - (titleWidth / 2);
        int titleTextY = (titleBgHeight / 2) + (titleFm.getAscent() - titleFm.getDescent()) / 2;
        g2d.drawString(titleText, titleTextX, titleTextY);
    }

    /**
     * 绘制水印的内容行（包括键值对形式的文本或整体文本行）。
     * 该方法遍历除了标题行之外的所有水印内容，并根据其类型（键值对或完整文本）在水印图像上进行绘制。
     *
     * @param g2d              绘图上下文，用于在水印图像上进行绘制操作。
     * @param watermarkContent 水印文本内容列表。列表的第一个元素被假定为标题，因此该方法从索引 1 开始遍历。
     * @param contentFont      用于绘制内容文本的字体。
     * @param titleBgHeight    标题背景区域的高度，内容行的起始Y坐标将基于此值计算。
     * @param maxKeyWidth      在所有键值对中，键（key）部分的最大宽度。这用于对齐值（value）部分的起始位置。
     * @param contentFm        内容字体的 FontMetrics 对象，用于获取内容文本的宽度和高度信息，以便计算文本位置和行间距。
     */
    private static void drawContentLines(Graphics2D g2d, List<WatermarkLine> watermarkContent, Font contentFont,
                                         int titleBgHeight, int maxKeyWidth, FontMetrics contentFm) {
        // 计算内容行的起始Y坐标：标题背景高度 + 上内边距
        int currentYPos = titleBgHeight + PADDING;
        // 遍历水印内容列表，从第二个元素（索引1）开始，因为第一个元素是标题。
        for (int i = 1; i < watermarkContent.size(); i++) {
            WatermarkLine line = watermarkContent.get(i); // 获取当前水印行的数据
            g2d.setFont(contentFont); // 设置当前行的字体为内容字体
            // 判断当前行是键值对类型还是整体文本类型
            if (line.key != null) {
                // 如果是键值对类型
                String keyWithColon = line.key + ": "; // 格式化键文本，加上冒号和空格
                g2d.setColor(CONTENT_TEXT_COLOR); // 设置键的颜色为默认内容文本颜色
                // 绘制键文本：位于左侧内边距处
                g2d.drawString(keyWithColon, PADDING, currentYPos + contentFm.getAscent());
                // 计算值的起始X坐标：左内边距 + 最大键宽度 + 键值对之间的固定间距，实现值的对齐
                int valueStartX = PADDING + maxKeyWidth + KEY_VAL_SPACING;
                // 根据 highlightValue 属性设置值的颜色
                if (line.highlightValue) {
                    g2d.setColor(HIGHLIGHT_TEXT_COLOR); // 高亮显示
                } else {
                    g2d.setColor(CONTENT_TEXT_COLOR); // 默认内容文本颜色
                }
                // 绘制值文本
                g2d.drawString(line.value, valueStartX, currentYPos + contentFm.getAscent());
            } else if (line.overallText != null) {
                // 如果是整体文本类型（如独立的描述行）
                g2d.setColor(CONTENT_TEXT_COLOR); // 设置文本颜色为默认内容文本颜色
                // 绘制整体文本
                g2d.drawString(line.overallText, PADDING, currentYPos + contentFm.getAscent());
            }
            // 更新下一行的Y坐标：当前行Y坐标 + 内容字体高度 + 行间距
            currentYPos += contentFm.getHeight() + LINE_SPACING;
        }
    }

    /**
     * 绘制公司信息行。
     *
     * @param g2d                              绘图上下文。
     * @param companyInfo                      公司信息字符串。
     * @param contentFont                      内容字体。
     * @param titleBgHeight                    标题背景高度。
     * @param contentTextLineCount             内容文本行数。
     * @param contentFm                        内容字体的 FontMetrics。
     */
    private static void drawCompanyInfo(Graphics2D g2d, String companyInfo, Font contentFont,
                                        int titleBgHeight, int contentTextLineCount,
                                        FontMetrics contentFm) {
        if (companyInfo != null && !companyInfo.isEmpty()) {
            g2d.setFont(contentFont);
            g2d.setColor(CONTENT_TEXT_COLOR);
            int currentYPos = titleBgHeight + PADDING; // 从内容区域的起始Y坐标开始
            // 累加内容行的总高度和行间距
            if (contentTextLineCount > 0) {
                currentYPos += (contentFm.getHeight() * contentTextLineCount);
                if (contentTextLineCount > 1) { // 只有多于一行内容时才有行间距
                    currentYPos += (contentTextLineCount - 1) * LINE_SPACING;
                }
                currentYPos += LINE_SPACING; // 公司信息行与上方内容的间距
            }
            // 绘制公司信息文本
            g2d.drawString(companyInfo, PADDING, currentYPos + contentFm.getAscent());
        }
    }

    /**
     * 将生成的水印图像应用到目标图片上并保存到指定路径。
     *
     * @param targetImg      目标图片。
     * @param position       水印在目标图片上的位置。
     * @param watermarkImage 要应用的水印图像。
     * @param opacity        水印的透明度。
     * @param outPath        输出图片的文件路径。
     * @throws IOException 如果在保存图片时发生I/O错误。
     */
    private static void applyAndSaveWatermark(BufferedImage targetImg, Positions position,
                                              BufferedImage watermarkImage, float opacity, String outPath) throws IOException {
        Thumbnails.of(targetImg)
                .size(targetImg.getWidth(), targetImg.getHeight())
                .watermark(position, watermarkImage, opacity)
                .outputQuality(1.0)
                .toFile(new File(outPath));
    }
}

