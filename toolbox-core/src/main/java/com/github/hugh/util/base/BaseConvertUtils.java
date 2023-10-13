package com.github.hugh.util.base;

import com.github.hugh.constant.CharsetCode;
import com.github.hugh.exception.ToolboxException;
import com.github.hugh.util.StringUtils;

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
     * hex 16进制能用到的所有字符 0-15
     */
    private static final String HEX_STRING = "0123456789ABCDEF";

    /**
     * hex char数组
     */
    private static final char[] HEX_ARRAY = HEX_STRING.toCharArray();

    /**
     * 十六进制 byte数组
     */
    private static final byte[] HEX_STRING_BYTE = HEX_STRING.getBytes();

    /**
     * 十进制字节数组转换成十六进制字符
     * <p>
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     * </p>
     * <p>默认返回结果都为大写</p>
     *
     * @param decBytes 十进制数组
     * @return String 十六进制的字符
     * @since 2.3.9
     */
    public static String hexBytesToString(byte[] decBytes) {
        return hexBytesToString(decBytes, null);
    }

    /**
     * 十进制字节数组转换成十六进制字符
     * <p>
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     * </p>
     * <p>默认返回结果都为大写</p>
     *
     * @param decBytes 十进制数组
     * @param interval 分隔符
     * @return String 十六进制的字符
     * @since 2.3.9
     */
    public static String hexBytesToString(byte[] decBytes, String interval) {
        boolean flag = false;
        if (interval != null) {
            flag = true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : decBytes) {
            stringBuilder.append(hexToString(b));
            if (flag) {
                stringBuilder.append(interval);
            }
        }
        if (flag) {
            return StringUtils.trimLastPlace(stringBuilder);
        }
        return stringBuilder.toString();
    }

    /**
     * 十进制字符串 转 十六进制数组
     *
     * @param decimal 十进制字符串
     * @return byte 十六进制数组
     * @since 2.3.9
     */
    public static byte[] decToHexBytes(String decimal) {
        return decToHexBytes(Integer.parseInt(decimal));
    }

    /**
     * 十进制数字 转 十六进制数组
     *
     * @param decimal 十进制数字
     * @return byte 十六进制数组
     * @since 2.3.9
     */
    public static byte[] decToHexBytes(int decimal) {
        String hexStr = decToHex(decimal);
        return hexToBytes(hexStr);
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
     * 十进制转16进制
     *
     * @param decimal 十进制
     * @return String 十六进制字符串
     * @since 1.7.0
     */
    public static String decToHex(String decimal) {
        return decToHex(Integer.parseInt(decimal));
    }

    /**
     * 将十进制整数转换为左起根据用户需求补0的十六进制字符串。
     *
     * @param decimal 十进制整数。
     * @param digits     转换后的十六进制字符串位数。
     * @return 左起根据用户需求补0的十六进制字符串。
     * @since 2.6.4
     */
    public static String decToHex(int decimal, int digits) {
        // 将十进制整数转换为十六进制字符串
        String hexStr = decToHex(decimal);
        // 基于用户需求，在左侧补0，生成指定位数的十六进制字符串
        return complement(hexStr, digits);
    }

    /**
     * 将十进制整数转换为左起根据用户需求补0的十六进制字符串。
     *
     * @param decimalStr 十进制整数字符串。
     * @param digits     转换后的十六进制字符串位数。
     * @return 左起根据用户需求补0的十六进制字符串。
     * @since 2.6.4
     */
    public static String decToHex(String decimalStr, int digits) {
        return decToHex(Integer.parseInt(decimalStr), digits);
    }

    /**
     * 10进制字节数组转换为16进制字节数组
     * <p>
     * byte用二进制表示占用8位，16进制的每个字符需要用4位二进制位来表示，则可以把每个byte
     * 转换成两个相应的16进制字符，即把byte的高4位和低4位分别转换成相应的16进制字符，再取对应16进制字符的字节
     *
     * @param decBytes 10进制字节数组
     * @return byte 16进制字节数组
     */
    public static byte[] decToHexBytes(byte[] decBytes) {
        int length = decBytes.length;
        byte[] b2 = new byte[length << 1];
        int pos;
        for (int i = 0; i < length; i++) {
            pos = 2 * i;
            b2[pos] = HEX_STRING_BYTE[(decBytes[i] & 0xf0) >> 4];
            b2[pos + 1] = HEX_STRING_BYTE[decBytes[i] & 0x0f];
        }
        return b2;
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
     * 十进制转二进制
     *
     * @param decimal 十进制
     * @return String 二进制
     * @since 2.4.1
     */
    public static String decToBinary(long decimal) {
        return Long.toBinaryString(decimal);
    }

    /**
     * 十进制转二进制
     * <p>默认左边不进行补位</p>
     *
     * @param str 字符串
     * @return String 二进制数字符串
     * @since 1.7.0
     */
    public static String decToBinary(String str) {
        return decToBinary(Long.parseLong(str));
    }

    /**
     * 十进制转二进制
     *
     * @param str    字符串
     * @param digits 二进制的位数（左边补零时才生效）
     * @return String
     * @since 1.7.0
     */
    public static String decToBinary(String str, int digits) {
        return decToBinary(Integer.parseInt(str), digits);
    }

    /**
     * 十进制转二进制
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 补0后的二进制
     */
    public static String decToBinary(int num, int digits) {
        String str = Long.toBinaryString(num);
        return complement(str, digits);
    }

    /**
     * 十进制转二进制
     *
     * @param num    需要转换的十进制数
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String 补0后的二进制
     * @since 2.4.1
     */
    public static String decToBinary(long num, int digits) {
        String str = Long.toBinaryString(num);
        return complement(str, digits);
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
     * 十进制字节数组转换成十六进制字符
     * <p>
     * 128位是指二进制位。二进制太长，所以一般都改写成16进制，
     * 每一位16进制数可以代替4位二进制数，所以128位二进制数写成16进制就变成了128/4=32位。
     * </p>
     * <p>默认返回结果都为大写</p>
     * <p>
     * 由于命名补规范，请使用{@link #hexBytesToString(byte[], String)}
     * </p>
     *
     * @param decBytes 十进制字节数组
     * @return String 十六进制字符串
     */
    @Deprecated
    public static String hexToString(byte[] decBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : decBytes) {
            stringBuilder.append(hexToString(b));
        }
        return stringBuilder.toString();
    }

    /**
     * 将一个十六进制字节转换成十六进制，并以字符串的形式返回
     *
     * @param byt 十六进制字节
     * @return String 十六进制字符
     */
    public static String hexToString(byte byt) {
        int n = byt;
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
     * 16进制的字符串转换为ascii
     *
     * @param hexStr   十六进制字符串
     * @param interval 切割的标识符
     * @return String
     * @since 1.4.9
     */
    @Deprecated
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
     * @since 1.7.0
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
     * @since 1.7.0
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
     * @since 1.7.0
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
     * @since 1.7.0
     */
    public static long hexToDec(String hex) {
        return Long.parseLong(hex, 16);
    }

    /**
     * 十六进制转二进制
     *
     * @param hex 十六进制
     * @return String
     * @since 2.4.1
     */
    public static String hexToBinaryStr(String hex) {
        return hexToBinaryStr(hex, 0);
    }

    /**
     * 十六进制转二进制
     *
     * @param hex    十六进制
     * @param digits 保留二进制的位数（左边补零时才生效）
     * @return String
     * @since 2.4.1
     */
    public static String hexToBinaryStr(String hex, int digits) {
        long l = hexToDec(hex);
        return decToBinary(l, digits);
    }

    /**
     * 十六进制 转 十进制字符串
     *
     * @param hex 十六进字符串
     * @return String 十进制
     * @since 1.7.0
     */
    public static String hexToDecString(String hex) {
        return String.valueOf(hexToDec(hex));
    }

    /**
     * 16进制字符串转十六进制的 byte[]
     * <p>如果Hex超过0xFF，显然转换后结果不是一个byte，而是一个byte数组</p>
     * <p>
     * 数组中的指为十六进制数，且如果首位是0，如：01、值为：1
     * </p>
     *
     * @param hexString 16进制字符串
     * @return byte  十六进制数组
     * @since 1.7.0
     */
    public static byte[] hexToBytes(String hexString) {
        int hexLen = hexString.length();
        byte[] result;
        if ((hexLen % 2) == 1) {//奇数
            hexLen++;
            result = new byte[(hexLen / 2)];
            hexString = "0" + hexString;
        } else {// 偶数
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
     * @return byte 十进制
     * @since 1.7.0
     */
    public static byte hexToByte(String hexString) {
        return (byte) Integer.parseInt(hexString, 16);
    }

    /**
     * 十六进制数组转ascii字符串
     * <p>
     * 默认编码{@link CharsetCode#GB_2312}
     * </p>
     *
     * @param hexBytes 十六进制数组
     * @return String ascii 的字符串
     * @since 2.3.9
     */
    public static String hexToAscii(byte[] hexBytes) {
        return hexToAscii(hexBytes, CharsetCode.GB_2312);
    }

    /**
     * 十六进制数组转ascii字符串
     *
     * @param hexBytes 十六进制数组
     * @param charset  编码名称
     * @return String ascii 的字符串
     * @since 2.3.9
     */
    public static String hexToAscii(byte[] hexBytes, String charset) {
        return hexToAscii(hexBytesToString(hexBytes), charset);
    }

    /**
     * 16进制直接转换成为ascii字符串(无需Unicode解码)
     * <p>默认十六进制字符串为大写、所以内部降字符串转换成大写</p>
     * <p>
     * 默认编码{@link CharsetCode#GB_2312}
     * </p>
     *
     * @param hexStr Byte字符串(Byte之间无分隔符
     * @return String ascii字符串
     * @since 1.7.0
     */
    public static String hexToAscii(String hexStr) {
        return hexToAscii(hexStr, CharsetCode.GB_2312);
    }

    /**
     * 16进制直接转换成为ascii字符串(无需Unicode解码)
     * <p>默认十六进制字符串为大写、所以内部降字符串转换成大写</p>
     *
     * @param hexStr  Byte字符串(Byte之间无分隔符
     * @param charset 编码格式
     * @return String ascii字符串
     * @since 1.7.0
     */
    public static String hexToAscii(String hexStr, String charset) {
        char[] hexChar = hexStr.toUpperCase().toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = HEX_STRING.indexOf(hexChar[position]) * 16;
            n += HEX_STRING.indexOf(hexChar[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            throw new ToolboxException(e);
        }
    }

    /**
     * ascii字符串转换成为16进制(无需Unicode编码)
     *
     * @param str 待转换的ASCII字符串
     * @return String 十六进制字符串
     * @since 1.7.0
     */
    public static String asciiToHex(String str) {
        return asciiToHex(str, null);
    }

    /**
     * ascii字符串转换成为16进制(无需Unicode编码)
     *
     * @param str   待转换的ASCII字符串
     * @param split 分隔符
     * @return String 十六进制字符串
     * @since 2.3.9
     */
    public static String asciiToHex(String str, String split) {
        return asciiToHex(str, split, CharsetCode.GB_2312);
    }

    /**
     * ascii字符串转换成为16进制(无需Unicode编码)
     * <p>splits 不为null 时则使用分隔符进行分割</p>
     *
     * @param str     ascii 码字符串
     * @param split   分隔符
     * @param charset 转换格式
     * @return String 十六进制字符串（每个Byte之间分隔符）
     * @since 1.7.1
     */
    public static String asciiToHex(String str, String split, String charset) {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bytes;
        try {
            //String的getBytes()方法是得到一个操作系统默认的编码格式的字节数组
            bytes = str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new ToolboxException(e);
        }
        int bit;
        for (byte b : bytes) {
            bit = (b & 0x0f0) >> 4; // 高4位, 与操作 1111 0000
            stringBuilder.append(HEX_ARRAY[bit]);
            bit = b & 0x0f;  // 低四位, 与操作 0000 1111
            stringBuilder.append(HEX_ARRAY[bit]);
            if (split != null) {
                stringBuilder.append(split);//拼接每个Byte之间分隔符
            }
        }
        if (split != null) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制数组转byte数组
     * <p>
     * 2022-7-23 就不该用这个方法，直接调用{@link  #hexToBytes}
     * </p>
     *
     * @param strings 十六进制字符串数组
     * @return byte 十进制数组
     * @since 1.7.6
     */
    @Deprecated
    public static byte[] hexArrToBytes(String[] strings) {
        byte[] bytes = new byte[strings.length];
        for (int i = 0; i < strings.length; i++) {
            bytes[i] = hexToByte(strings[i]);
        }
        return bytes;
    }

    /**
     * 十六进制字符串转byte数组
     *
     * <p>
     * 2022-7-23 就不该用这个方法，直接调用{@link  #hexToAscii}
     * </p>
     * <p>
     * 如果有存在空格，则线{@link String#replace(char, char)}掉所有空格，再调用{@link  #hexToBytes}
     * </p>
     *
     * @param string 十六进制字符串
     * @param split  字符串中分隔符
     * @return byte
     * @since 1.7.6
     */
    @Deprecated
    public static byte[] hexArrToBytes(String string, String split) {
        return hexArrToBytes(string.split(split));
    }

    /**
     * 16进制字节数组转换为10进制字节数组
     * <p>
     * 两个16进制字节对应一个10进制字节，则将第一个16进制字节对应成16进制字符表中的位置(0~15)并向左移动4位，
     * 再与第二个16进制字节对应成16进制字符表中的位置(0~15)进行或运算，则得到对应的10进制字节
     *
     * @param hexBytes 16进制字节数组
     * @return byte 10进制字节数组
     */
    public static byte[] hexToDec(byte[] hexBytes) {
        if ((hexBytes.length % 2) != 0) {
            throw new IllegalArgumentException("byte array length is not even!");
        }
        int length = hexBytes.length >> 1;
        byte[] b2 = new byte[length];
        int pos;
        for (int i = 0; i < length; i++) {
            pos = i << 1;
            b2[i] = (byte) (HEX_STRING.indexOf(hexBytes[pos]) << 4 | HEX_STRING.indexOf(hexBytes[pos + 1]));
        }
        return b2;
    }

    /**
     * 十六进制数组转int
     *
     * @param hexBytes 十六进制byte数组
     * @return int
     * @since 2.3.9
     */
    public static int hexToDecReInt(byte[] hexBytes) {
        String s = hexBytesToString(hexBytes);
        return (int) hexToDec(s);
    }

    /**
     * 将字符串转换为十六进制表示形式
     *
     * @param input 要转换的字符串
     * @return 转换后的十六进制字符串
     * @since 2.6.1
     */
    public static String strToHex(String input) {
        StringBuilder hexString = new StringBuilder();
        for (char c : input.toCharArray()) {
            hexString.append(Integer.toHexString(c));
        }
        return hexString.toString();
    }

    /**
     * 将十六进制字符串转换为 Base64 编码的字符串。
     *
     * @param hexStr 十六进制字符串
     * @return Base64 编码的字符串
     * @since 2.6.1
     */
    public static String hexToBase64(String hexStr) {
        return new String(hexToBase64Bytes(hexStr));
    }

    /**
     * 将十六进制字符串转换为 Base64 编码的字节数组。
     *
     * @param hexStr 十六进制字符串
     * @return Base64 编码的字节数组
     * @since 2.6.1
     */
    public static byte[] hexToBase64Bytes(String hexStr) {
        byte[] bytes = BaseConvertUtils.hexToBytes(hexStr);
        return Base64.encode(bytes);
    }
}