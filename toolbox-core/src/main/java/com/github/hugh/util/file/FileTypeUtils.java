package com.github.hugh.util.file;

import com.github.hugh.constant.enums.FileTypeEnum;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.BaseConvertUtils;
import com.github.hugh.util.io.StreamUtils;

import java.io.*;

/**
 * 文件类型判断工具类
 *
 * @author hugh
 * @since 2.6.0
 */
public class FileTypeUtils {


    /**
     * 获取文件真实类型
     *
     * @param filePath 文件路径
     * @return String 文件类型
     * @throws FileNotFoundException 文件未找到异常
     */
    public static String getType(String filePath) throws FileNotFoundException {
        return getType(new File(filePath));
    }

    /**
     * 获取文件真实类型
     * <p>
     *
     * </p>
     *
     * @param file 文件
     * @return String 文件类型
     * @throws FileNotFoundException 文件未找到异常
     */
    public static String getType(File file) throws FileNotFoundException {
        return getType(new FileInputStream(file), true);
    }

    /**
     * 根据输入流获取文件类型。
     * <p>
     * 该方法不会关闭{@link InputStream}，如需获取完后关闭流则调用{@link  #getType(InputStream, boolean)}
     * </p>
     *
     * @param inputStream 输入流
     * @return 文件类型，如果无法确定类型则返回null
     */
    public static String getType(InputStream inputStream) {
        return getType(inputStream, false);
    }

    /**
     * 获取文件真实类型
     * <p>zip文件头与xlsx近似，无法准确判断</p>
     *
     * @param inputStream 输入流
     * @param closeStream 是否关闭输入流，true表示关闭，false表示不关闭
     * @return String 文件类型，如果无法确定类型则返回null
     */
    public static String getType(InputStream inputStream, boolean closeStream) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            String fileHead = getFileHead(bis);
            if (EmptyUtils.isEmpty(fileHead)) {
                return null;
            }
            FileTypeEnum[] fileTypes = FileTypeEnum.values();
            for (FileTypeEnum type : fileTypes) {
                if (fileHead.startsWith(type.getValue())) {
                    return type.name().toLowerCase();
                }
            }
            return null;
        } catch (IOException ioException) {
            throw new ToolboxException(ioException.getMessage());
        } finally {
            if (closeStream) {
                StreamUtils.close(inputStream);
            }
        }
    }

    /**
     * 获取文件流中文件头内容，并且转十六进制
     *
     * @param inputStream 文件流
     * @return String 文件头十六进制
     */
    private static String getFileHead(InputStream inputStream) {
        byte[] bytes = new byte[28];
        try {
            inputStream.read(bytes, 0, 28);
        } catch (IOException ioException) {
            throw new ToolboxException(ioException);
        }
        return BaseConvertUtils.hexBytesToString(bytes);
    }
}
