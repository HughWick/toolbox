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
        String s = suffix.toUpperCase();
        return SuffixCode.PNG.equals(s) || SuffixCode.JPG.equals(s) ||
                SuffixCode.JPEG.equals(s) || SuffixCode.BMP.equals(s) ||
                SuffixCode.GIF.equals(s) || SuffixCode.SVG.equals(s);
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
        String s = suffix.toUpperCase();
        return SuffixCode.ZIP.equals(s) || SuffixCode.RAR.equals(s)
                || SuffixCode.GZ.equals(s) || SuffixCode.TYPE_7Z.equals(s);
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
        String s = suffix.toUpperCase();
        return SuffixCode.XLS.equals(s) || SuffixCode.XLSX.equals(s);
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
        String s = suffix.toUpperCase();
        return SuffixCode.DOC.equals(s) || SuffixCode.DOCX.equals(s);
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
        return SuffixCode.MP4.equals(suffix.toUpperCase());
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
        return SuffixCode.PDF.equals(suffix.toUpperCase());
    }
}
