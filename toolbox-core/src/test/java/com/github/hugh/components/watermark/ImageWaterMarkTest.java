package com.github.hugh.components.watermark;

import com.github.hugh.util.TimeUtils;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageWaterMarkTest {
    /**
     * 从类路径加载字体文件。
     *
     * @param fontPath  类路径下的字体文件路径，例如 "fonts/Microsoft_YaHei.ttf"。
     * @param fontSize  字体大小。
     * @param fontStyle 字体样式（Font.PLAIN, Font.BOLD, Font.ITALIC）。
     * @return 加载的 Font 对象，如果失败则返回 null。
     * @throws IOException         如果字体文件无法读取。
     * @throws FontFormatException 如果字体文件格式不正确。
     */
    private static Font loadFontFromClasspath(String fontPath, int fontSize, int fontStyle) throws IOException, FontFormatException {
        try (InputStream is = ImageWaterMarkTest.class.getClassLoader().getResourceAsStream(fontPath)) {
            if (is == null) {
                System.err.println("未在类路径中找到字体文件: " + fontPath);
                return null; // 或者抛出 IllegalArgumentException
            }
            // 使用 Font.createFont 从 InputStream 创建 Font 对象
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, is); // 假设是 TTF 字体
            // 基于创建的字体设置样式和大小
            return baseFont.deriveFont(fontStyle, (float) fontSize);
        }
    }

    /**
     * 为图片添加具有复杂样式的多行水印（模拟工程记录UI样式）。
     * 旧的整体工具类，暂时保留一份存档
     */
//    public static void addComplexWatermark(ComplexWatermarkBuilder complexWatermarkBuilder) throws IOException { // 新增 companyInfo 参数
//        // --- 1. 计算水印区域的尺寸 ---
//        BufferedImage tempGfxImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2dTemp = tempGfxImage.createGraphics();
//        g2dTemp.setFont(complexWatermarkBuilder.getTitleFont());
//        FontMetrics titleFm = g2dTemp.getFontMetrics();
//        g2dTemp.setFont(complexWatermarkBuilder.getContentFont());
//        FontMetrics contentFm = g2dTemp.getFontMetrics();
//        int padding = 20; // 整个水印UI的左右内边距，也用于内容区域的上下内边距
//        int titleVerticalPadding = 10; // 标题文本到蓝色背景上下边缘的距离，可调整此值来控制蓝色区域的高度
//        int lineSpacing = 10; // 内容行之间的间距
//        int iconRadius = 5; // 左上角圆点半径
//        String titleText = complexWatermarkBuilder.getWatermarkContent().get(0).overallText;
//        int titleWidth = titleFm.stringWidth(titleText);
//        int titleHeight = titleFm.getHeight(); // 标题文本的实际高度
//        int contentTextLineCount = complexWatermarkBuilder.getWatermarkContent().size() - 1;
//        int contentLinesHeightExcludingPadding = 0; // 只计算文本内容和行间距的总高度，不包含外部padding
//        int maxKeyWidth = 0;
//        int maxValWidth = 0;
//        if (contentTextLineCount > 0) {
//            for (int i = 1; i < complexWatermarkBuilder.getWatermarkContent().size(); i++) {
//                WatermarkLine line = complexWatermarkBuilder.getWatermarkContent().get(i);
//                if (line.key != null) {
//                    maxKeyWidth = Math.max(maxKeyWidth, contentFm.stringWidth(line.key + ": "));
//                    maxValWidth = Math.max(maxValWidth, contentFm.stringWidth(line.value));
//                }
//                contentLinesHeightExcludingPadding += contentFm.getHeight();
//            }
//            // 加上内容行之间的间距
//            if (contentTextLineCount > 1) {
//                contentLinesHeightExcludingPadding += (contentTextLineCount - 1) * lineSpacing;
//            }
//        }
//        int keyValSpacing = 15;
//        int maxContentLineWidth = 0;
//        if (contentTextLineCount > 0) {
//            maxContentLineWidth = maxKeyWidth + keyValSpacing + maxValWidth;
//        }
//        // --- 新增：计算公司信息行的尺寸，并更新总高度 ---
//        int companyInfoHeight = 0;
//        int companyInfoWidth = 0;
//        int totalContentAndCompanyTextHeight = contentLinesHeightExcludingPadding; // 初始化为内容文本高度
//
//        if (complexWatermarkBuilder.getCompanyInfo() != null && !complexWatermarkBuilder.getCompanyInfo().isEmpty()) {
//            companyInfoHeight = contentFm.getHeight();
//            companyInfoWidth = contentFm.stringWidth(complexWatermarkBuilder.getCompanyInfo());
//            // 如果之前有内容行，需要加上公司信息行与上一行的间距
//            if (contentTextLineCount > 0) {
//                totalContentAndCompanyTextHeight += lineSpacing;
//            }
//            totalContentAndCompanyTextHeight += companyInfoHeight; // 加上公司信息行的高度
//            maxContentLineWidth = Math.max(maxContentLineWidth, companyInfoWidth); // 公司信息宽度也可能影响水印总宽度
//        }
//        // 最终的白色内容区域总高度：内容文本和公司信息的高度 + 上下内边距
//        int contentTotalHeight = 0;
//        if (totalContentAndCompanyTextHeight > 0) {
//            contentTotalHeight = totalContentAndCompanyTextHeight + padding * 2;
//        }
//        int watermarkWidth = Math.max(titleWidth, maxContentLineWidth) + padding * 2;
//        int titleBgHeight = titleHeight + titleVerticalPadding * 2; // 标题背景的高度
//        int watermarkHeight = titleBgHeight + contentTotalHeight; // 总水印高度
//        g2dTemp.dispose();
//        BufferedImage complexWatermarkImage = new BufferedImage(watermarkWidth, watermarkHeight, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = complexWatermarkImage.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        int arcSize = 20; // 圆角弧度
//        // --- 3. 绘制背景矩形 ---
//        // 3.1 绘制整个水印的底层蓝色圆角矩形。这将作为蓝色背景和整体圆角的基础。
//        g2d.setColor(title_background_color);
//        g2d.fillRoundRect(0, 0, watermarkWidth, watermarkHeight, arcSize, arcSize);
//
//        // 3.2 在蓝色背景之上绘制白色内容区域。
//        if (contentTotalHeight > 0) {
//            g2d.setColor(content_background_color);
//            // 绘制白色背景的圆角矩形，使其底部有圆角。
//            g2d.fillRoundRect(0, titleBgHeight, watermarkWidth, contentTotalHeight, arcSize, arcSize);
//            // 填充白色背景的顶部，使其与蓝色背景平滑衔接（直线）。
//            g2d.fillRect(0, titleBgHeight, watermarkWidth, contentTotalHeight - arcSize);
//        }
//        // --- 4. 绘制左上角黄色小圆点 ---
//        g2d.setColor(ICON_COLOR);
//        int iconY = (titleBgHeight / 2) - iconRadius;
//        g2d.fillOval(padding / 2, iconY, iconRadius * 2, iconRadius * 2);
//
//        // --- 5. 绘制标题 (例如 "工程记录") ---
//        g2d.setFont(complexWatermarkBuilder.getTitleFont());
//        g2d.setColor(DEFAULT_TEXT_COLOR); // 标题文本颜色 (白色)
//        int titleTextX = (watermarkWidth / 2) - (titleWidth / 2);
//        int titleTextY = (titleBgHeight / 2) + (titleFm.getAscent() - titleFm.getDescent()) / 2;
//        g2d.drawString(titleText, titleTextX, titleTextY); // 绘制标题文本，现在水平居中
//
//        // --- 6. 绘制内容行 ---
//        int currentYPos = titleBgHeight + padding; // 内容起始 Y 坐标为蓝色区域底部 + 上内边距
//        for (int i = 1; i < complexWatermarkBuilder.getWatermarkContent().size(); i++) {
//            WatermarkLine line = complexWatermarkBuilder.getWatermarkContent().get(i);
//            g2d.setFont(complexWatermarkBuilder.getContentFont());
//            if (line.key != null) {
//                String keyWithColon = line.key + ": ";
//                g2d.setColor(CONTENT_TEXT_COLOR);
//                g2d.drawString(keyWithColon, padding, currentYPos + contentFm.getAscent());
//                int valueStartX = padding + maxKeyWidth + keyValSpacing;
//                if (line.highlightValue) {
//                    g2d.setColor(HIGHLIGHT_TEXT_COLOR);
//                } else {
//                    g2d.setColor(CONTENT_TEXT_COLOR);
//                }
//                g2d.drawString(line.value, valueStartX, currentYPos + contentFm.getAscent());
//            } else if (line.overallText != null) {
//                g2d.setColor(CONTENT_TEXT_COLOR);
//                g2d.drawString(line.overallText, padding, currentYPos + contentFm.getAscent());
//            }
//            currentYPos += contentFm.getHeight() + lineSpacing;
//        }
//
//        // --- 7. 绘制公司信息行 ---
//        if (complexWatermarkBuilder.getCompanyInfo() != null && !complexWatermarkBuilder.getCompanyInfo().isEmpty()) {
//            // 如果之前有内容行，需要加上公司信息行与上一行的间距
//            if (contentTextLineCount > 0) {
//                currentYPos = currentYPos - lineSpacing + lineSpacing; // 回退一个lineSpacing，然后加上companyInfoLineSpacing
//            } else {
//                // 如果没有内容行，直接从白色区域顶部内边距开始计算
//                currentYPos = titleBgHeight + padding;
//            }
//            g2d.setFont(complexWatermarkBuilder.getContentFont()); // 使用内容字体
//            g2d.setColor(CONTENT_TEXT_COLOR); // 使用内容文本颜色
//            g2d.drawString(complexWatermarkBuilder.getCompanyInfo(), padding, currentYPos + contentFm.getAscent());
//        }
//        g2d.dispose();
//        Thumbnails.of(complexWatermarkBuilder.getTargetImage())
//                .size(complexWatermarkBuilder.getTargetImage().getWidth(), complexWatermarkBuilder.getTargetImage().getHeight())
//                .watermark(complexWatermarkBuilder.getPosition(), complexWatermarkImage, complexWatermarkBuilder.getOpacity())
//                .outputQuality(1.0)
//                .toFile(new File(complexWatermarkBuilder.getOutPath()));
//    }
//    @Test
//    void testWater() {
//        // --- 示例用法 ---
//        Path tempDir = null; // 用于存储临时输出文件的目录
//        try {
//            // 1. 创建一个临时目录用于存放测试输出文件
//            tempDir = Files.createTempDirectory("watermark_test_output_");
//            System.out.println("测试输出目录: " + tempDir.toAbsolutePath());
//            // 假设你的背景图片路径（测试时可以使用测试资源目录下的图片）
//            // 如果你的图片在 src/test/resources 下，可以这样获取路径
//            // String inputImagePath = getClass().getClassLoader().getResource("test_image.jpg").getPath();
//            // 为了简化，这里仍然使用您的D盘路径，但实际项目中应将其移入测试资源
//            String inputImagePath = "D:\\360极速浏览器X下载\\4ac38054b49c421ab47b5397693d8965.jpg"; // 请替换为你的实际图片路径
//            // 构造输出文件的完整路径
//            String outputFileName1 = "output_image_with_complex_watermark.jpg";
//            String outputFileName2 = "output_image_with_complex_watermark2.jpg";
//
//            Path outputPath1 = tempDir.resolve(outputFileName1);
//            Path outputPath2 = tempDir.resolve(outputFileName2);
//            // 加载目标图片
//            BufferedImage targetImage = ImageWaterMark.loadImage(inputImagePath);
//            // 定义水印内容
//            List<WatermarkLine> content = new ArrayList<>();
//            content.add(new WatermarkLine("工程记录")); // 标题
//            content.add(new WatermarkLine("天  气", "阴 高温 35℃", true)); // 突出显示“高温 35℃”
//            content.add(new WatermarkLine("地  点", "湖南省湘西花垣县猫儿乡086乡道", false));
//            content.add(new WatermarkLine("海  拔", "273.3米", false));
//            content.add(new WatermarkLine("经  度", "109.929794°E", false));
//            content.add(new WatermarkLine("纬  度", "28.734864°N", false));
//            // 定义字体
//            int titleFontSize = 24;
//            int contentFontSize = 18;
//            String titleFontName = "SourceHanSansCN-Bold.otf";
//            String contentFontName = "SourceHanSansCN-Regular.otf";
//            // 从类路径加载字体
//            Font titleFont = loadFontFromClasspath("fonts/" + titleFontName, titleFontSize, Font.BOLD);
//            Font contentFont = loadFontFromClasspath("fonts/" + contentFontName, contentFontSize, Font.PLAIN);
//            if (titleFont == null || contentFont == null) {
//                System.err.println("错误：未能加载所需字体。请确保字体文件存在于 'src/main/resources/fonts/' 目录下，并且名称正确。");
//                // 可以选择抛出异常或使用默认字体
//                titleFont = new Font("SansSerif", Font.BOLD, titleFontSize); // 使用默认字体作为备用
//                contentFont = new Font("SansSerif", Font.PLAIN, contentFontSize); // 使用默认字体作为备用
//            }
////            Font titleFont = new Font("Microsoft YaHei", Font.BOLD, 24); // 标题字体
////            Font contentFont = new Font("Microsoft YaHei", Font.PLAIN, 18); // 内容字体
//            // 添加复杂水印
//            ImageWaterMark.addComplexWatermark(targetImage, content, titleFont, contentFont, 0.8f, Positions.BOTTOM_LEFT, outputPath1.toString(), "@湖南同恒信息技术有限公司");
//            ImageWaterMark.addComplexWatermark(targetImage, content, titleFont, contentFont, 0.8f, Positions.BOTTOM_LEFT, outputPath2.toString(), "");
//            System.out.println("复杂水印图片已生成: " + outputPath1.toAbsolutePath());
//            System.out.println("复杂水印图片已生成: " + outputPath2.toAbsolutePath());
//
//            // 可以在这里添加断言，例如检查文件是否存在，或者（如果需要）检查文件大小等
//            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath1));
//            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath2));
//
//        } catch (IOException e) {
//            System.err.println("处理图片时发生错误: " + e.getMessage());
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.err.println("发生未知错误: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            // 2. 在测试结束后清理临时目录
//            if (tempDir != null && Files.exists(tempDir)) {
//                try {
//                    // 使用 Files.walk 和 Files.delete 删除目录及其内容
//                    Files.walk(tempDir)
//                            .sorted(Comparator.reverseOrder()) // 引入 Comparator
//                            .forEach(path -> { // 使用 Lambda 表达式
//                                try {
//                                    Files.deleteIfExists(path); // 使用 Files.deleteIfExists
//                                } catch (IOException e) {
//                                    System.err.println("无法删除文件/目录 " + path + ": " + e.getMessage());
//                                    // 可以在这里根据需要进一步处理，例如抛出运行时异常
//                                }
//                            });
//                    System.out.println("已清理临时目录: " + tempDir.toAbsolutePath());
//                } catch (IOException e) {
//                    System.err.println("清理临时目录失败: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    @Test
    void testWatermarkGeneration() {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("watermark_test_output_");
            System.out.println("测试输出目录: " + tempDir.toAbsolutePath());

            String inputImagePath = "D:\\360极速浏览器X下载\\4ac38054b49c421ab47b5397693d8965.jpg";
            String outputFileName1 = "output_image_with_complex_watermark.jpg";
            String outputFileName2 = "output_image_with_complex_watermark2.jpg";
            String outputFileName3 = "output_image_with_complex_watermark3.jpg";

            Path outputPath1 = tempDir.resolve(outputFileName1);
            Path outputPath2 = tempDir.resolve(outputFileName2);
            Path outputPath3 = tempDir.resolve(outputFileName3);
            List<WatermarkLine> content = new ArrayList<>();
            content.add(new WatermarkLine("工程记录"));
            content.add(new WatermarkLine("天  气", "阴 高温 35℃", true));
            content.add(new WatermarkLine("地  点", "湖南省湘西花垣县猫儿乡086乡道", false));
            content.add(new WatermarkLine("海  拔", "273.3米", false));
            content.add(new WatermarkLine("经  度", "109.929794°E", false));
            content.add(new WatermarkLine("纬  度", "28.734864°N", false));
            content.add(new WatermarkLine("时  间", TimeUtils.now(), false));
//            int titleFontSize = 24;
//            int contentFontSize = 18;
//            String titleFontName = "SourceHanSansCN-Bold.otf";
//            String contentFontName = "SourceHanSansCN-Regular.otf";
//
//            Font titleFont = loadFontFromClasspath("fonts/" + titleFontName, titleFontSize, Font.BOLD);
//            Font contentFont = loadFontFromClasspath("fonts/" + contentFontName, contentFontSize, Font.PLAIN);
//
//            if (titleFont == null || contentFont == null) {
//                System.err.println("错误：未能加载所需字体。请确保字体文件存在于 'src/main/resources/fonts/' 目录下，并且名称正确。");
//                titleFont = new Font("SansSerif", Font.BOLD, titleFontSize);
//                contentFont = new Font("SansSerif", Font.PLAIN, contentFontSize);
//            }
            // ================================================================
            // === 核心修改：使用 ComplexWatermarkBuilder 构建参数并调用新方法 ===
            // ================================================================

            // 调用方式一：使用 Lombok @Builder 自动生成的 builder() 方法
            // 这也是最推荐的方式
            // 构建第一个水印的配置
            ComplexWatermarkBuilder builder1 = new ComplexWatermarkBuilder() // 实例化
                    .setTargetImage(inputImagePath) // 使用 setXxx 链式调用
                    .setWatermarkContent(content)
//                    .setTitleFont(titleFont)
//                    .setContentFont(contentFont)
//                    .setOpacity(0.8f)
//                    .setPosition(Positions.BOTTOM_LEFT)
                    .setOutPath(outputPath1.toString())
                    .setCompanyInfo("@湖南同恒信息技术有限公司");
            // 或者一个包含配置的普通对象（如果@Builder在方法上或自定义了build方法）

            // 调用 ImageWaterMark 中接受 ComplexWatermarkBuilder 的重载方法
            ImageWaterMark.addComplex(builder1);
            System.out.println("复杂水印图片已生成: " + outputPath1.toAbsolutePath());

            // ----------------------------------------------------------------
            // 调用方式二：第二个水印，无公司信息
            ComplexWatermarkBuilder builder2 = new ComplexWatermarkBuilder()
                    .setTargetImage(new File(inputImagePath))
                    .setWatermarkContent(content)
//                    .setTitleFont(titleFont)
//                    .setContentFont(contentFont)
//                    .setOpacity(0.8f)
//                    .setPosition(Positions.BOTTOM_LEFT)
                    .setOutPath(outputPath2.toString())
                    .setCompanyInfo(""); // 不设置公司信息，或设置为 ""

            ImageWaterMark.addComplex(builder2);
            System.out.println("复杂水印图片已生成: " + outputPath2.toAbsolutePath());
            // ================================================================
            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath1));
            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath2));
            // ================================================================
            // 新增测试用例：通过 InputStream 加载图片
            ComplexWatermarkBuilder builder3 = new ComplexWatermarkBuilder()
                    .setTargetImage(Files.newInputStream(Path.of(inputImagePath)))
                    .setWatermarkContent(content)
                    .setOpacity(0.9f)
//                    .setPosition(Positions.CENTER) // 设置不同位置以区分
                    .setOutPath(outputPath3.toString())
                    .setCompanyInfo("测试来自文件流"); // 设置公司信息以区分
            ImageWaterMark.addComplex(builder3);
            System.out.println("复杂水印图片已生成 (InputStream加载): " + outputPath3.toAbsolutePath());
            // ================================================================
        } catch (IOException e) {
            System.err.println("处理图片时发生错误: " + e.getMessage());
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail("测试失败：处理图片时发生IOException " + e.getMessage());
        } catch (Exception e) {
            System.err.println("发生未知错误: " + e.getMessage());
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail("测试失败：发生未知错误 " + e.getMessage());
        } finally {
//            if (tempDir != null && Files.exists(tempDir)) {
//                try {
//                    Files.walk(tempDir)
//                            .sorted(Comparator.reverseOrder())
//                            .forEach(path -> {
//                                try {
//                                    Files.deleteIfExists(path);
//                                } catch (IOException e) {
//                                    System.err.println("无法删除文件/目录 " + path + ": " + e.getMessage());
//                                }
//                            });
//                    System.out.println("已清理临时目录: " + tempDir.toAbsolutePath());
//                } catch (IOException e) {
//                    System.err.println("清理临时目录失败: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
        }
    }

}
