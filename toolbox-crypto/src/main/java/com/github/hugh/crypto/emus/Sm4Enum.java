package com.github.hugh.crypto.emus;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * sm 加密类型枚举类
 *
 * @author hugh
 * @since 2.6.1
 */
@Getter
@AllArgsConstructor
public enum Sm4Enum {

    // 算法名称
    ALGORITHM_NAME("SM4"),
    BASE64("base64"),
    // 文本
    TEXT("TEXT"),
    // 十六进制
    HEX("HEX"),
    // 加密模式 cbc
    ALGORITHM_NAME_CBC_PADDING("SM4/CBC/PKCS7Padding"),
    // 加密模式 ecb
    ALGORITHM_NAME_ECB_PKCS5_PADDING("SM4/ECB/PKCS5Padding"),
    ALGORITHM_NAME_ECB_NO_PADDING("SM4/ECB/NoPadding"),

    ;

    private String code;

}
