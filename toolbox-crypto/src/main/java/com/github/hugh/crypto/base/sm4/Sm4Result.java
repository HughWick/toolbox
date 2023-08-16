package com.github.hugh.crypto.base.sm4;

import com.github.hugh.util.base.Base64;
import com.github.hugh.util.base.BaseConvertUtils;

/**
 * 封装SM4加密结果的类
 *
 * @author hugh
 * @since 2.6.1
 */
public class Sm4Result {

    private final byte[] bytes;

    /**
     * 构造方法，用于初始化加密结果
     *
     * @param bytes 加密结果的字节数组
     */
    public Sm4Result(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 将加密结果转换为十六进制字符串表示形式
     *
     * @return 十六进制字符串
     */
    public String toHex() {
        return BaseConvertUtils.hexBytesToString(this.bytes);
    }

    /**
     * 将加密结果转换为Base64字符串表示形式
     *
     * @return Base64字符串
     */
    public String toBase64() {
        return Base64.encodeToString(this.bytes);
    }
}
