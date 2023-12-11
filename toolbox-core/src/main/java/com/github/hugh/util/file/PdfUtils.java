package com.github.hugh.util.file;

import com.github.hugh.constant.StrPool;
import com.github.hugh.exception.ToolboxException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PDF文件工具类
 *
 * @author hugh
 * @since 2.6.8
 */
public class PdfUtils {

    /**
     * @param source      原文件
     * @param desFilePath 生成图片的路径
     * @param desFileName 生成图片的名称（多页文档时会变成：名称+下划线+从1开始的数字）
     * @param imageType   图片类型
     * @return 生成图片的集合
     */
    public static List<String> pdfToImage(String source, String desFilePath, String desFileName, String imageType) {
        //通过给定的源路径名字符串创建一个File实例
        File file = new File(source);
        if (!file.exists()) {
            throw new ToolboxException("file not exist ");
        }
        //目录不存在则创建目录
        File destination = new File(desFilePath);
        if (!destination.exists()) {
            destination.mkdirs();
        }
        try (PDDocument doc = PDDocument.load(file)) {            //加载PDF文件
            PDFRenderer renderer = new PDFRenderer(doc);
            //获取PDF文档的页数
            int pageCount = doc.getNumberOfPages();
            List<String> fileList = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                //只有一页的时候文件名为传入的文件名，大于一页的文件名为：文件名_自增加数字(从1开始)
                String realFileName = pageCount > 1 ? desFileName + "_" + (i + 1) : desFileName;
                //每一页通过分辨率和颜色值进行转化
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 96 * 2, ImageType.RGB);
                String filePath = desFilePath + File.separator + realFileName + StrPool.POINT + imageType;
                //写入文件
                ImageIO.write(bufferedImage, imageType, new File(filePath));
                //文件名存入list
                fileList.add(filePath);
            }
            return fileList;
        } catch (IOException ioException) {
            throw new ToolboxException(ioException.getMessage());
        }
    }
}
