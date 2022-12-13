package com.github.hugh.util.file;

import com.github.hugh.constant.SuffixCode;
import com.github.hugh.util.EmptyUtils;

/**
 * 文件后缀工具类
 *
 * @author hugh
 * @since 1.5.8
 */
public class SuffixUtils {

    /**
     * 校验后缀是否时图片格式
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isImage(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.PNG.equalsIgnoreCase(suffix) || SuffixCode.JPG.equalsIgnoreCase(suffix)
                || SuffixCode.JPEG.equalsIgnoreCase(suffix) || SuffixCode.BMP.equalsIgnoreCase(suffix)
                || SuffixCode.GIF.equalsIgnoreCase(suffix) || SuffixCode.SVG.equalsIgnoreCase(suffix)
                || SuffixCode.WEBP.equalsIgnoreCase(suffix);
    }

    /**
     * 校验后缀是否为压缩包格式
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isCompress(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.ZIP.equalsIgnoreCase(suffix) || SuffixCode.RAR.equalsIgnoreCase(suffix)
                || SuffixCode.GZ.equalsIgnoreCase(suffix) || SuffixCode.TYPE_7Z.equalsIgnoreCase(suffix);
    }

    /**
     * 文件后缀是否为excel 类型
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isExcel(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.XLS.equalsIgnoreCase(suffix) || SuffixCode.XLSX.equalsIgnoreCase(suffix);
    }

    /**
     * 文件后缀为word文档类型
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isWord(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.DOC.equalsIgnoreCase(suffix) || SuffixCode.DOCX.equalsIgnoreCase(suffix);
    }

    /**
     * 文件后缀为mp4 视频类型
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isMp4(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.MP4.equalsIgnoreCase(suffix);
    }

    /**
     * 文件后缀为mp4 视频类型
     *
     * @param suffix 文件后缀
     * @return boolean
     */
    public static boolean isPdf(String suffix) {
        if (EmptyUtils.isEmpty(suffix)) {
            return false;
        }
        return SuffixCode.PDF.equalsIgnoreCase(suffix);
    }
}
