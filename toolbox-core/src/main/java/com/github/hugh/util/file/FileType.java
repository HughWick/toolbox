package com.github.hugh.util.file;

import com.github.hugh.constant.SuffixCode;
import com.github.hugh.constant.enums.FileTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 文件类型判断工具类
 *
 * @author hugh
 * @since 2.6.0
 */
public class FileType {

    private final InputStream inputStream;
    private final boolean closeStream;

    /**
     * 使用给定的文件路径创建一个FileType对象。
     *
     * @param filePath 文件路径
     * @throws FileNotFoundException 如果文件不存在或不可读取，则抛出该异常。
     */
    public FileType(String filePath) throws FileNotFoundException {
        this(new File(filePath));
    }

    /**
     * 使用给定的File对象创建一个FileType对象。
     *
     * @param file 文件对象
     * @throws FileNotFoundException 如果文件不存在或不可读取，则抛出该异常。
     */
    public FileType(File file) throws FileNotFoundException {
        this(new FileInputStream(file), true);
    }

    /**
     * 使用给定的InputStream对象创建一个FileType对象。
     *
     * @param inputStream 输入流对象
     */
    public FileType(InputStream inputStream, boolean closeStream) {
        this.inputStream = inputStream;
        this.closeStream = closeStream;
    }

    /**
     * 创建一个使用给定文件路径的FileType对象。
     *
     * @param filePath 文件路径
     * @return FileType对象
     * @throws FileNotFoundException 如果文件不存在或不可读取，则抛出该异常。
     */
    public static FileType on(String filePath) throws FileNotFoundException {
        return new FileType(filePath);
    }

    /**
     * 创建一个使用给定File对象的FileType对象。
     *
     * @param file 文件对象
     * @return FileType对象
     * @throws FileNotFoundException 如果文件不存在或不可读取，则抛出该异常。
     */
    public static FileType on(File file) throws FileNotFoundException {
        return new FileType(file);
    }

    /**
     * 创建一个使用给定InputStream对象的FileType对象。
     * <p>该方法调用方式默认会关闭流</p>
     *
     * @param inputStream 输入流对象
     * @return FileType对象
     */
    public static FileType on(InputStream inputStream) {
        return new FileType(inputStream, true);
    }

    /**
     * 根据输入流判断文件类型的工具方法。
     *
     * @param inputStream 输入流
     * @param closeStream 是否关闭输入流。如果为 true，在判断文件类型后将关闭输入流；如果为 false，需要手动关闭输入流。
     * @return 文件类型对象
     */
    public static FileType on(InputStream inputStream, boolean closeStream) {
        return new FileType(inputStream, closeStream);
    }


    /**
     * 判断文件是否为JPEG格式（后缀名为.jpg）
     *
     * @return 如果是JPEG格式，则返回true；否则返回false
     */
    public boolean isJpg() {
        return SuffixCode.JPG_LOWER_CASE.equals(getType());
    }

    /**
     * 判断文件是否为PNG格式（后缀名为.png）
     *
     * @return 如果是PNG格式，则返回true；否则返回false
     */
    public boolean isPng() {
        return SuffixCode.PNG.equals(getType());
    }

    /**
     * 判断文件是否为WebP格式（后缀名为.webp）
     *
     * @return 如果是WebP格式，则返回true；否则返回false
     */
    public boolean isWebp() {
        return SuffixCode.WEBP.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为PDF格式（后缀名为.pdf）
     *
     * @return 如果是PDF格式，则返回true；否则返回false
     */
    public boolean isPdf() {
        return SuffixCode.PDF.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为MP4格式（后缀名为.mp4）
     *
     * @return 如果是MP4格式，则返回true；否则返回false
     */
    public boolean isMp4() {
        return SuffixCode.MP4.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为MP3格式（后缀名为.mp3）
     *
     * @return 如果是MP3格式，则返回true；否则返回false
     */
    public boolean isMp3() {
        return SuffixCode.MP3.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为Office 2007格式（包括xlsx和docx）
     *
     * @return 如果是Office 2007格式，则返回true；否则返回false
     */
    public boolean isOffice2007() {
        return isXlsx() || isDocx();
    }

    /**
     * 判断文件是否为xlsx格式（后缀名为.xlsx）
     *
     * @return 如果是xlsx格式，则返回true；否则返回false
     */
    public boolean isXlsx() {
        return SuffixCode.XLSX.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为docx格式（后缀名为.docx）
     *
     * @return 如果是docx格式，则返回true；否则返回false
     */
    public boolean isDocx() {
        return SuffixCode.DOCX.equalsIgnoreCase(getType());
    }

    /**
     * 判断文件是否为Office 2003格式（包括xls和doc）
     *
     * @return 如果是Office 2003格式，则返回true；否则返回false
     */
    public boolean isOffice2003() {
        return FileTypeEnum.XLS_DOC.name().equalsIgnoreCase(getType());
    }

    /**
     * 检查当前文件类型是否为 ZIP 文件。
     *
     * @return 如果当前文件类型是 ZIP，则返回 true；否则返回 false。
     */
    public boolean isZip() {
        return SuffixCode.ZIP.equalsIgnoreCase(getType());
    }

    /**
     * 检查当前文件类型是否为 RAR 文件。
     *
     * @return 如果当前文件类型是 RAR，则返回 true；否则返回 false。
     */
    public boolean isRar() {
        return SuffixCode.RAR.equalsIgnoreCase(getType());
    }

    /**
     * 获取文件的类型
     *
     * @return 文件类型，如"jpg"、"png"等
     */
    public String getType() {
        return FileTypeUtils.getType(this.inputStream, this.closeStream);
    }
}
