package com.github.hugh.crypto.base.sm4;

import com.github.hugh.util.base.Base64;
import com.github.hugh.util.base.BaseConvertUtils;
import lombok.Getter;

/**
 * 封装SM4加密结果的类
 *
 * @author hugh
 * @since 2.6.1
 */
@Getter
public class Sm4Result {

    private final byte[] bytes;

    /**
     * 构造函数，用于创建 Sm4Result 实例。
     *
     * @param bytes SM4 加解密操作后得到的原始字节数组。
     */
    public Sm4Result(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 将 SM4 加解密操作后得到的字节数组转换为十六进制字符串。
     *
     * @return 转换后的十六进制字符串。
     */
    public String toHex() {
        return BaseConvertUtils.hexBytesToString(this.bytes);
    }

    /**
     * 将 SM4 加解密操作后得到的字节数组转换为 Base64 编码的字符串。
     *
     * @return 转换后的 Base64 编码字符串。
     */
    public String toBase64() {
        return Base64.encodeToString(this.bytes);
    }
}
