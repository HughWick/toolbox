package com.github.hugh.components.watermark;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import lombok.extern.slf4j.Slf4j;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;

/**
 * 一个用于处理图片方向的工具类。
 */
@Slf4j
public class ImageOrientationCorrector {

    private ImageOrientationCorrector() {
    }

    /**
     * 根据图片的 EXIF Orientation 标签校正 BufferedImage 的方向。
     *
     * @param image       要校正的 BufferedImage 对象。
     * @param imageSource 原始图片来源（File 或 InputStream）。为了保证能够重复读取，
     *                    传入的 InputStream 会被完整读入内存。
     * @return 方向正确的 BufferedImage。如果无需校正或无法读取元数据，则返回原始图像。
     * @throws IOException 如果读取图片源时发生 I/O 错误。
     */
    public static BufferedImage correct(BufferedImage image, Object imageSource) throws IOException {
        if (image == null || imageSource == null) {
            return image;
        }
        // 为了能同时读取 EXIF 和图像数据，我们将输入源统一处理为可重复读的 InputStream
        byte[] imageBytes = readImageSourceToBytes(imageSource);
        if (imageBytes == null) {
            return image; // 不支持的源类型或读取失败
        }
        try {
            Optional<Integer> orientationOpt = readExifOrientation(new ByteArrayInputStream(imageBytes));
            // 如果没有方向信息或方向正常，则直接返回原始图像
            if (orientationOpt.isEmpty() || orientationOpt.get() == 1) {
                return image;
            }
            int orientation = orientationOpt.get();
            AffineTransform transform = getExifTransformation(orientation, image.getWidth(), image.getHeight());
            // 如果变换为空 (对于 orientation=1 或未知情况)，则不处理
            if (transform.isIdentity()) {
                return image;
            }
            AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
            boolean swapDimensions = orientation >= 5 && orientation <= 8;
            int newWidth = swapDimensions ? image.getHeight() : image.getWidth();
            int newHeight = swapDimensions ? image.getWidth() : image.getHeight();
            // 如果原始图像类型未知，则使用一个支持透明度的标准类型
            int imageType = (image.getType() == BufferedImage.TYPE_CUSTOM) ? BufferedImage.TYPE_INT_ARGB : image.getType();
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, imageType);
            // 应用变换
            return op.filter(image, newImage);
        } catch (Exception exception) {
            log.error("Error reading image metadata: " + exception.getMessage());
            // 发生异常时返回原始图像，保证流程不中断
            return image;
        }
    }

    /**
     * 将不同的图片源 (File, InputStream) 统一读取为字节数组。
     *
     * @param imageSource 图片源
     */
    private static byte[] readImageSourceToBytes(Object imageSource) throws IOException {
        if (imageSource instanceof File) {
            try (InputStream in = new FileInputStream((File) imageSource)) {
                return in.readAllBytes();
            }
        } else if (imageSource instanceof InputStream) {
            // 直接读取流，调用者需注意传入的流将被消耗
            return ((InputStream) imageSource).readAllBytes();
        }
        return null;
    }

    /**
     * 从输入流中读取 EXIF 方向标签。
     *
     * @param inputStream 图片数据的输入流。
     * @return 包含方向值的 Optional，如果找不到则为空。
     */
    private static Optional<Integer> readExifOrientation(InputStream inputStream) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                return Optional.of(directory.getInt(ExifIFD0Directory.TAG_ORIENTATION));
            }
        } catch (Exception exception) {
            log.error("Error reading image metadata: " + exception.getMessage());
        }
        return Optional.empty();
    }

    /**
     * 根据 EXIF 方向值计算所需的仿射变换。
     *
     * @param orientation EXIF 方向值 (1-8)。
     * @param width       图像宽度。
     * @param height      图像高度。
     * @return 计算好的 AffineTransform 对象。
     */
    private static AffineTransform getExifTransformation(int orientation, int width, int height) {
        AffineTransform affineTransform = new AffineTransform();
        switch (orientation) {
            case 1: // 正常
                break;
            case 2: // 水平翻转
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-width, 0);
                break;
            case 3: // 旋转180度
                affineTransform.translate(width, height);
                affineTransform.rotate(Math.PI);
                break;
            case 4: // 垂直翻转
                affineTransform.scale(1.0, -1.0);
                affineTransform.translate(0, -height);
                break;
            case 5: // 逆时针旋转90度 + 垂直翻转
                affineTransform.rotate(-Math.PI / 2);
                affineTransform.scale(-1.0, 1.0);
                break;
            case 6: // 逆时针旋转90度
                affineTransform.translate(height, 0);
                affineTransform.rotate(Math.PI / 2);
                break;
            case 7: // 顺时针旋转90度 + 水平翻转
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-height, 0);
                affineTransform.translate(0, width);
                affineTransform.rotate(Math.PI / 2);
                break;
            case 8: // 顺时针旋转90度
                affineTransform.translate(0, width);
                affineTransform.rotate(-Math.PI / 2);
                break;
        }
        return affineTransform;
    }
}