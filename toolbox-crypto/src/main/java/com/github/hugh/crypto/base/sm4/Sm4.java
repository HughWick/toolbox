package com.github.hugh.crypto.base.sm4;

import com.github.hugh.crypto.util.Sm4Utils;
import com.github.hugh.crypto.emus.Sm4Enum;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.EmptyUtils;
import com.github.hugh.util.base.Base64;
import com.github.hugh.util.base.BaseConvertUtils;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * SM4 国标加密
 *
 * @author hugh
 * @since 2.6.1
 */
public class Sm4 {

    /**
     * -- GETTER --
     * 获取加密密钥
     * <p>
     * 该方法返回当前对象中存储的加密密钥字符串。
     * </p>
     */
    @Getter
    private final String key;
    private String encryptKeyType; // 加密key 数据类型

    private String encryptResultType;// 加密/解密结果 数据类型


    /**
     * 构造方法，使用给定的密钥初始化 Sm4 对象。
     *
     * @param key 密钥字符串。
     */
    public Sm4(String key) {
        this.key = key;
    }

    /**
     * 创建一个新的 Sm4 对象，并使用给定的密钥进行初始化。
     *
     * @param key 密钥字符串。
     * @return 初始化后的 Sm4 对象。
     */
    public static Sm4 on(String key) {
        return new Sm4(key);
    }

    /**
     * 创建一个新的 Sm4 对象，并随机生成一个密钥进行初始化。
     * 生成的密钥将转换为十六进制字符串。
     *
     * @return 初始化后的 Sm4 对象。
     */
    public static Sm4 on() {
        byte[] bytes = Sm4Utils.generateKey();
        return new Sm4(BaseConvertUtils.hexBytesToString(bytes));
    }

    /**
     * 设置加密类型为文本类型。
     * 使用文本类型时，待加密和解密的数据应为普通字符串。
     *
     * @return 当前 Sm4 对象。
     */
    public Sm4 encryptKeyTypeByStr() {
        this.encryptKeyType = Sm4Enum.TEXT.getCode();
        return this;
    }


    public Sm4 encryptResultTypeByHex() {
        this.encryptResultType = Sm4Enum.HEX.getCode();
        return this;
    }

    /**
     * 设置加密类型为 Base64 类型。
     * 使用 Base64 类型时，待加密和解密的数据应为 Base64 编码字符串。
     *
     * @return 当前 Sm4 对象。
     */
    public Sm4 encryptKeyTypeByBase64() {
        this.encryptKeyType = Sm4Enum.BASE64.getCode();
        return this;
    }

    /**
     * 设置加密密钥类型为 HEX（十六进制）
     * <p>
     * 该方法将当前对象的加密密钥类型设置为 `Sm4Enum.HEX.getCode()`，表示密钥类型为十六进制格式。
     * </p>
     *
     * @return 返回当前对象（`Sm4`）本身，支持链式调用。
     */
    public Sm4 encryptKeyTypeByHex() {
        this.encryptKeyType = Sm4Enum.HEX.getCode();
        return this;
    }

    /**
     * 使用 ECB 模式对文本进行加密。
     *
     * @param text 待加密的文本。
     * @return 包含加密结果的 Sm4Result 对象。
     */
    public Sm4Result encryptEcb(String text) {
        return encrypt(text, null);
    }

    /**
     * 使用 CBC 模式对文本进行加密。
     *
     * @param text 待加密的文本。
     * @param iv   初始化向量，用于 CBC 模式的加密操作。
     * @return 包含加密结果的 Sm4Result 对象。
     */
    public Sm4Result encryptCbc(String text, String iv) {
        return encrypt(text, iv);
    }

    /**
     * 使用SM4算法进行加密
     *
     * @param text 待加密的明文字符串
     * @param iv   初始化向量（可选）
     * @return 加密结果对象，包含加密后的密文字符串
     * @throws ToolboxException 如果发生加密异常或提供的密钥类型无效，则抛出该异常
     */
    private Sm4Result encrypt(String text, String iv) {
        byte[] keyBytes = getKeyOrIvBytes(this.key, this.encryptKeyType);
        byte[] ivBytes = null; // 存储初始化向量的字节数组（可选）
        if (iv != null) {
            ivBytes = getKeyOrIvBytes(iv, this.encryptKeyType);
        }
        return ivBytes == null
                ? new Sm4Result(Sm4Utils.encryptEcbPadding(keyBytes, text))
                : new Sm4Result(Sm4Utils.encryptCbcPadding(keyBytes, ivBytes, text));
    }

    /**
     * 使用 ECB 模式进行解密。
     *
     * @param cipherText 待解密的密文
     * @return 解密后的明文
     */
    public String decryptEcb(String cipherText) {
        return decryptPadding(cipherText, null);
    }

    /**
     * 使用 CBC 模式进行解密。
     *
     * @param cipherText 待解密的密文
     * @param iv         初始向量
     * @return 解密后的明文
     * @throws ToolboxException 如果初始向量为空，则抛出异常
     */
    public String decryptCbc(String cipherText, String iv) {
        if (EmptyUtils.isEmpty(iv)) {
            throw new ToolboxException("iv is null");
        }
        return decryptPadding(cipherText, iv);
    }

    /**
     * 使用指定的填充方式进行解密。
     *
     * @param cipherText 待解密的密文，必须是base64格式
     * @param iv         初始向量
     * @return 解密后的明文
     */
    private String decryptPadding(String cipherText, String iv) {
        // 存储密钥的字节数组
        byte[] keyBytes = getKeyOrIvBytes(this.key, this.encryptKeyType);
        byte[] ivBytes = null; // 存储初始化向量的字节数组（可选）
        if (iv != null) {
            ivBytes = getKeyOrIvBytes(iv, this.encryptKeyType);
        }
        byte[] cipherTextByte;
        // 根据加密结果类型进行处理
        if (Sm4Enum.HEX.getCode().equals(encryptResultType)) {
            cipherTextByte = BaseConvertUtils.hexToBytes(cipherText); // 将十六进制字符串转换为字节数组作为待解密的密文
        } else {
            cipherTextByte = Base64.decodeToByte(cipherText); // 将Base64编码的字符串解码为字节数组作为待解密的密文
        }
        // 分析是否需要使用初始化向量进行解密
        return ivBytes == null
                ? Sm4Utils.decryptEcbPadding(keyBytes, cipherTextByte) // 使用ECB模式解密（无初始化向量）
                : Sm4Utils.decryptCbcPadding(keyBytes, ivBytes, cipherTextByte); // 使用CBC模式解密（包含初始化向量）
    }

    /**
     * 根据加密密钥类型获取对应的字节数组密钥或初始化向量（IV）。
     *
     * @param text           加密密钥或初始化向量字符串（可以是明文、十六进制字符串或Base64编码字符串）
     * @param encryptKeyType 加密密钥类型（TEXT、HEX或BASE64）
     * @return 对应的字节数组密钥或初始化向量
     * @throws ToolboxException 如果加密密钥类型错误或者字节数组长度小于16字节，则抛出异常
     */
    private byte[] getKeyOrIvBytes(String text, String encryptKeyType) {
        byte[] keyBytes;
        if (Sm4Enum.TEXT.getCode().equals(encryptKeyType)) {
            if (text.getBytes().length < 16) {
                throw new ToolboxException("密钥长度不能少于 16 字节！");
            }
            keyBytes = Arrays.copyOfRange(text.getBytes(), 0, 16); // 获取前16个字节作为密钥
        } else if (Sm4Enum.HEX.getCode().equals(encryptKeyType)) {
            keyBytes = BaseConvertUtils.hexToBytes(text); // 将十六进制字符串转换为字节数组作为密钥
        } else if (Sm4Enum.BASE64.getCode().equals(encryptKeyType)) {
            keyBytes = Base64.decodeToByte(text); // 将Base64编码的字符串解码为字节数组作为密钥
        } else {
            throw new ToolboxException("错误加密key类型"); // 抛出异常，表示加密密钥类型错误
        }
        return keyBytes;
    }

    /**
     * 使用 ECB 模式对密文进行解密，并与指定的明文进行比较验证是否一致。
     *
     * @param cipherText 待解密的密文
     * @param plainText  用于比较验证的明文
     * @return 如果解密后的数据与明文一致，则返回 true；否则返回 false。
     */
    public boolean verifyEcb(String cipherText, String plainText) {
        byte[] decryptData = decryptEcb(cipherText).getBytes(StandardCharsets.UTF_8);
        byte[] srcData = plainText.getBytes(StandardCharsets.UTF_8);
        return Arrays.equals(decryptData, srcData);
    }

    /**
     * 使用 ECB 模式对密文进行解密，并与指定的明文进行比较验证是否一致。
     *
     * @param cipherText 待解密的密文
     * @param plainText  用于比较验证的明文
     * @return 如果解密后的数据与明文一致，则返回 true；否则返回 false。
     */
    public boolean verifyCbc(String cipherText, String iv, String plainText) {
        // 使用给定的密钥和IV解密CBC填充的密文
        String decryptedCbcText = decryptCbc(cipherText, iv);
        // 将解密后的文本转换成字节数组
        byte[] decryptData = decryptedCbcText.getBytes(StandardCharsets.UTF_8);
        // 将原始明文转换成字节数组
        byte[] srcData = plainText.getBytes(StandardCharsets.UTF_8);
        // 比较解密数据和原始明文的字节数组是否相等
        return Arrays.equals(decryptData, srcData);
    }
}
