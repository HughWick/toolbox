package com.github.hugh.util.base;

import com.github.hugh.constant.CharsetCode;
import com.github.hugh.exception.ToolboxException;

import java.io.UnsupportedEncodingException;

/**
 * 进制转换工具类
 *
 * @author hugh
 * @since 1.1.0
 */
public class BaseConvertUtils {

    private BaseConvertUtils() {
    }

    /**
     * hex 数组
     */
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * 将字节数组转换成十六进制，并以字符串的形式返回
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     * <p>默认为大写</p>
     *
     * @param bytes 字节数组
     * @return String 字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(byteToHex(b));
        }
        return sb.toString();
    }

    /**
     * 将一个字节转换成十六进制，并以字符串的形式返回
     *
     * @param b 比特
     * @return String
     */
    public static String byteToHex(byte b) {
        int n = b;
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        final String charOne = String.valueOf(HEX_ARRAY[d1]);
        final String charTwo = String.valueOf(HEX_ARRAY[d2]);
        return charOne + charTwo;
    }

    /**
     * 十进制转16进制
     *
     * @param decimal 十进制
     * @return String 十六进制字符串
     */
    public static String decToHex(int decimal) {
        return Integer.toHexString(decimal);
    }

    /**
     * 十进制转二进制
     *
     * @param decimal 十进制
     * @return String 二进制
     */
    public static String decToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    /**
     * 二进制转十进制
     *
     * @param binary 二进制
     * @return String  十进制
     */
    public static String binaryToDec(String binary) {
        return Integer.valueOf(binary, 2).toString();
    }

    /**
     * 十进制转二进制
     * <p>默认左边不进行补位</p>
     *
     * @param str 字符串
     * @return String 二进制数字符串
     * @since 1.6.14
     */
    public static String toBinary(String str) {
        return toBinary(Integer.parseInt(str), 0);
    }

    /**
     * 十进制转二进制
     *
     * @param str    字符串
     * @param digits 二进制的位数（左边补零时才生效）
     * @return String
     * @since 1.6.14
     */
    public static String toBinary(String str, int digits) {
        return toBinary(Integer.parseInt(str), digits);
    }

    /**
     * 十进制转二进制
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 补0后的二进制
     */
    public static String toBinary(int num, int digits) {
        String str = Integer.toBinaryString(num);
        return complement(str, digits);
    }

    /**
     * 16进制转换为字符串
     *
     * @param hexStr   十六进制字符串
     * @param interval 切割的标识符
     * @return String
     * @since 1.4.9
     */
    public static String hexToString(String hexStr, String interval) {
        if (interval == null) {
            throw new ToolboxException(" interval is null ");
        }
        String[] array = hexStr.split(interval);
        byte[] bytes = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            bytes[i] = Byte.parseByte(array[i], 16);
        }
        return new String(bytes);
    }

    /**
     * 十六进制转二进制 并且补位
     * <p>默认左边不进行补位、只做16进制转换为二进制</p>
     *
     * @param num 需要转换的十进制数
     * @return String 二进制字符串
     * @since 1.6.14
     */
    public static String hexToBinary(String num) {
        return hexToBinary(num, 0);
    }

    /**
     * 十六进制转二进制 并且补位
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 二进制字符串
     * @since 1.6.14
     */
    public static String hexToBinary(String num, int digits) {
        String str = Integer.toBinaryString(Integer.valueOf(num, 16));
        return complement(str, digits);
    }

    /**
     * 转换二进制后 根据传入的不为数值 左边补0
     *
     * @param str    字符串
     * @param digits 补位数
     * @return String 补位后的二进制
     * @since 1.6.14
     */
    private static String complement(String str, int digits) {
        String cover = Integer.toBinaryString(1 << digits).substring(1);
        return str.length() < digits ? cover.substring(str.length()) + str : str;
    }

    /**
     * 十六进制转十进制
     *
     * @param hex 十六进字符串
     * @return int 十进制
     * @since 1.6.14
     */
    public static int hexToDec(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 十六进制 转 十进制字符串
     *
     * @param hex 十六进字符串
     * @return String 十进制
     * @since 1.6.14
     */
    public static String hexToDecString(String hex) {
        return String.valueOf(hexToDec(hex));
    }

    /**
     * 16进制数转byte[]
     * <p>如果Hex超过0xFF，显然转换后结果不是一个byte，而是一个byte数组</p>
     *
     * @param hexString 16进制字符串
     * @return byte[]
     * @since 1.6.14
     */
    public static byte[] hexToBytes(String hexString) {
        int hexLen = hexString.length();
        byte[] result;
        if (hexLen % 2 == 1) {
            //奇数
            hexLen++;
            result = new byte[(hexLen / 2)];
            hexString = "0" + hexString;
        } else {
            //偶数
            result = new byte[(hexLen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexLen; i += 2) {
            result[j] = hexToByte(hexString.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 十六进制字符串转byte
     *
     * @param hexString 待转换的Hex字符串
     * @return byte
     * @since 1.6.14
     */
    public static byte hexToByte(String hexString) {
        return (byte) Integer.parseInt(hexString, 16);
    }

    /**
     * 16进制直接转换成为ascii字符串(无需Unicode解码)
     *
     * @param hexStr Byte字符串(Byte之间无分隔符
     * @return String
     * @since 1.6.14
     */
    public static String hexToAscii(String hexStr) {
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        try {
            return new String(bytes, CharsetCode.GB_2312);
        } catch (UnsupportedEncodingException e) {
            throw new ToolboxException(e);
        }
    }

    /**
     *  ascii字符串转换成为16进制(无需Unicode编码)
     *
     * @param str 待转换的ASCII字符串
     * @return String byte字符串 （每个Byte之间空格分隔）
     * @since 1.6.14
     */
    public static String asciiToHex(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs;//String的getBytes()方法是得到一个操作系统默认的编码格式的字节数组
        try {
            bs = str.getBytes(CharsetCode.GB_2312);
        } catch (UnsupportedEncodingException e) {
            throw new ToolboxException(e);
        }
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4; // 高4位, 与操作 1111 0000
            sb.append(HEX_ARRAY[bit]);
            bit = b & 0x0f;  // 低四位, 与操作 0000 1111
            sb.append(HEX_ARRAY[bit]);
            sb.append(' ');//每个Byte之间空格分隔
        }
        return sb.toString().trim();
    }
}
