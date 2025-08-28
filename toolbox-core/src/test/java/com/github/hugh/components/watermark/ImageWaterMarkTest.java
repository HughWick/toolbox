package com.github.hugh.components.watermark;

import com.github.hugh.util.TimeUtils;
import net.coobird.thumbnailator.geometry.Positions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ImageWaterMarkTest {
    private List<WatermarkLine> standardWatermarkContent;

    // 使用 @TempDir，JUnit5 会自动管理临时目录的创建和销毁
//    @TempDir
    Path tempDir;

    // 在每个测试方法执行前，都初始化一份标准的水印内容
    @BeforeEach
    void setUp() throws IOException {
        standardWatermarkContent = new ArrayList<>();
        standardWatermarkContent.add(new WatermarkLine("工程记录"));
        standardWatermarkContent.add(new WatermarkLine("天  气", "阴 高温 35℃", true)); // 测试高亮
        standardWatermarkContent.add(new WatermarkLine("地  点", "湖南省湘西花垣县猫儿乡086乡道", false));
        standardWatermarkContent.add(new WatermarkLine("海  拔", "273.3米", false));
        standardWatermarkContent.add(new WatermarkLine("经  度", "109.929794°E", false));
        standardWatermarkContent.add(new WatermarkLine("纬  度", "28.734864°N", false));
        standardWatermarkContent.add(new WatermarkLine("时  间", TimeUtils.now(), false));
        tempDir = Files.createTempDirectory("test_watermark_output_");
//        System.out.println("测试输出目录: " + tempDir.toAbsolutePath());
    }

    @Test
    void testWatermarkGeneration() {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("watermark_test_output_");
            System.out.println("测试输出目录: " + tempDir.toAbsolutePath());
            String inputImagePath1 = "/file/watermark/593c3fc5592e42d09da01edf57fd0b79.jpg";
            String inputImagePath2 = "/file/watermark/5085ef6c750a48a49107e2b0a117ca4c.jpg";
            String inputImagePath3 = "/file/watermark/8eaab21ebd1a4d658920bf771de4e7dd.jpg";
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
//            Font titleFont = loadFontFromClasspath("fonts/" + titleFontName, titleFontSize, Font.BOLD);
//            Font contentFont = loadFontFromClasspath("fonts/" + contentFontName, contentFontSize, Font.PLAIN);
//            if (titleFont == null || contentFont == null) {
//                System.err.println("错误：未能加载所需字体。请确保字体文件存在于 'src/main/resources/fonts/' 目录下，并且名称正确。");
//                titleFont = new Font("SansSerif", Font.BOLD, titleFontSize);
//                contentFont = new Font("SansSerif", Font.PLAIN, contentFontSize);
//            }
            // ================================================================
            // === 核心修改：使用 ComplexWatermarkBuilder 构建参数并调用新方法 ===
            // ================================================================
            String path1 = getPath(inputImagePath1);
            // 调用方式一：使用 Lombok @Builder 自动生成的 builder() 方法
            // 这也是最推荐的方式
            // 构建第一个水印的配置
            ComplexWatermarkBuilder builder1 = new ComplexWatermarkBuilder() // 实例化
                    .setTargetImage(path1) // 使用 setXxx 链式调用
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
            System.out.println("复杂水印图片已生成（公司签名）: " + outputPath1.toAbsolutePath());
            String path2 = getPath(inputImagePath2);
            // ----------------------------------------------------------------
            // 调用方式二：第二个水印，无公司信息
            ComplexWatermarkBuilder builder2 = new ComplexWatermarkBuilder()
                    .setTargetImage(new File(path2))
                    .setWatermarkContent(content)
//                    .setTitleFont(titleFont)
//                    .setContentFont(contentFont)
//                    .setOpacity(0.8f)
//                    .setPosition(Positions.BOTTOM_LEFT)
                    .setOutPath(outputPath2.toString())
                    .setCompanyInfo(""); // 不设置公司信息，或设置为 ""

            ImageWaterMark.addComplex(builder2);
            System.out.println("复杂水印图片已生成（没有公司签名）: " + outputPath2.toAbsolutePath());
            // ================================================================
            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath1));
            org.junit.jupiter.api.Assertions.assertTrue(Files.exists(outputPath2));
            String path3 = getPath(inputImagePath3);
            FileInputStream fileInputStream = new FileInputStream(path3);
            // ================================================================
            // 新增测试用例：通过 InputStream 加载图片
            ComplexWatermarkBuilder builder3 = new ComplexWatermarkBuilder()
                    .setTargetImage(fileInputStream)
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

    @Test
    @DisplayName("测试默认设置（左下角，无边距），通过文件路径加载")
    void testDefaultPositionWithStringPath() throws IOException {
        String inputImagePath1 = "/file/watermark/864_1980/e0dfddbcae6328e0124b9d63e11b4159.jpg";
        Path outputPath = tempDir.resolve("output_default_bottom_left.jpg");
        ComplexWatermarkBuilder builder = new ComplexWatermarkBuilder()
                .setTargetImage(getPath(inputImagePath1))
                .setWatermarkContent(standardWatermarkContent)
                .setOutPath(outputPath.toString())
                .setCompanyInfo("测试公司信息");
        generateWatermarkAndAssert(builder, outputPath);
        System.out.println("复杂水印图片已生成（公司签名）: " + outputPath.toAbsolutePath());
    }

    @Test
    @DisplayName("测试不设置公司信息")
    void testWithoutCompanyInfo() throws IOException {
        String inputImagePath2 = "/file/watermark/960_1280/47d728e0cd5f8298997676703d93610f.jpg";
        Path outputPath = tempDir.resolve("output_without_company_info.jpg");
        ComplexWatermarkBuilder builder = new ComplexWatermarkBuilder()
                .setTargetImage(getPath(inputImagePath2))
                .setWatermarkContent(standardWatermarkContent)
                .setOutPath(outputPath.toString())
                .setCompanyInfo(null); // 明确设置为null或空字符串
        generateWatermarkAndAssert(builder, outputPath);
        System.out.println("复杂水印图片已生成（没有公司签名）: " + outputPath.toAbsolutePath());
    }

    @Test
    @DisplayName("测试通过 InputStream 加载图片")
    void testLoadingFromInputStream() throws IOException {
        String inputImagePath3 = "/file/watermark/3072_4096/dbdda5027c87787ee31da4f4a5c7e292.jpg";
        Path outputPath = tempDir.resolve("output_from_input_stream.jpg");
        try (FileInputStream fileInputStream = new FileInputStream(getPath(inputImagePath3))) {
            ComplexWatermarkBuilder builder = new ComplexWatermarkBuilder()
                    .setTargetImage(fileInputStream)
                    .setWatermarkContent(standardWatermarkContent)
                    .setOutPath(outputPath.toString());
            generateWatermarkAndAssert(builder, outputPath);
            System.out.println("复杂水印图片已生成 (InputStream加载): " + outputPath.toAbsolutePath());
        }
    }

    @Test
    @DisplayName("测试右下角定位，并附带右边距和下边距")
    void testBottomRightPositionWithMargins() throws IOException {
        String inputImagePath5 = "/file/watermark/1920_1440/0e24bb04e2a500316bd2e636851cd13b.jpg";
        Path outputPath = tempDir.resolve("output_default_bottom_right.jpg");
        ComplexWatermarkBuilder builder = new ComplexWatermarkBuilder()
                .setTargetImage(getPath(inputImagePath5))
                .setWatermarkContent(standardWatermarkContent)
                .setOutPath(outputPath.toString())
                .setPosition(Positions.BOTTOM_RIGHT) // 明确设置为右下角
                .setMarginRight(50)                  // 设置右边距
                .setMarginBottom(50)                 // 设置下边距
                .setCompanyInfo("@右下角测试");
        generateWatermarkAndAssert(builder, outputPath);
        System.out.println("图片右下角复杂水印图片已生成: " + outputPath.toAbsolutePath());
    }

    @Test
    @DisplayName("测试默认设置（左下角，无边距），通过文件流")
    void testDefaultPositionWithInputStream() throws IOException {
        String inputImagePath4 = "/file/watermark/3072_4096/dbdda5027c87787ee31da4f4a5c7e292.jpg";
        Path outputPath = tempDir.resolve("output_default_bottom_left.jpg");
        ComplexWatermarkBuilder builder = new ComplexWatermarkBuilder()
                .setTargetImage(getPath(inputImagePath4))
                .setWatermarkContent(standardWatermarkContent)
                .setOutPath(outputPath.toString())
                .setCompanyInfo("测试公司信息");
        generateWatermarkAndAssert(builder, outputPath);
        System.out.println("带自定义边距复杂水印图片已生成 (InputStream加载): " + outputPath.toAbsolutePath());
    }

    /**
     * 辅助方法，用于执行水印生成并进行通用断言
     */
    private void generateWatermarkAndAssert(ComplexWatermarkBuilder builder, Path outputPath) throws IOException {
        ImageWaterMark.addComplex(builder);
        System.out.println("水印图片已生成: " + outputPath.toAbsolutePath());
        Assertions.assertAll(
                () -> Assertions.assertTrue(Files.exists(outputPath), "输出文件应存在"),
                () -> Assertions.assertTrue(Files.size(outputPath) > 0, "输出文件不应为空")
        );
    }

    private static String getPath(String fileName) {
        return ImageWaterMarkTest.class.getResource(fileName).getPath();
    }
}
