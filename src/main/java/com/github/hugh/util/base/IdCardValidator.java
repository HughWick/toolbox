package com.github.hugh.util.base;

import com.github.hugh.util.EmptyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 身份证验证
 * <p>该类主要作用于身份证号码验证，如需使用其他身份证相关工具方法参见{@link com.github.hugh.util.common.IdCardUtils}</p>
 *
 * @author hugh
 * @since 1.2.8
 */
public class IdCardValidator {

    /**
     * <pre>
     *
     * 省、直辖市代码表：
     *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
     *     21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏
     *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
     *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
     *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
     *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
     *     71 : 台湾
     *     81 : 香港  82 : 澳门
     *     91 : 国外
     * </pre>
     */
    private static String[] cityCode = {"11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42", "43",
            "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91"};

    /**
     * 每位加权因子
     */
    private static int[] power = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 校验18位身份证合法性
     *
     * @param idCard 18位身份证
     * @return boolean 合法返回true，否则返回false
     */
    public static boolean is18Place(String idCard) {
        if (EmptyUtils.isEmpty(idCard)) {
            return false;
        }
        if (idCard.length() != 18) { // 非18位
            return false;
        }
        String idCard17 = idCard.substring(0, 17); // 获取前17位
        if (!isDigital(idCard17)) { // 前17位全部为数字
            return false;
        }
        String provinceId = idCard.substring(0, 2);
        if (!checkProvinceId(provinceId)) { // 校验省份
            return false;
        }
        String birthday = idCard.substring(6, 14); // 校验出生日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {// 出生年月日不正确
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }
        String idCard18Code = idCard.substring(17, 18); // 获取第18位
        char[] c = idCard17.toCharArray();
        int[] bit = converCharToInt(c);
        int sum17 = getPowerSum(bit);
        String checkCode = getCheckCodeBySum(sum17);// 将和值与11取模得到余数进行校验码判断
        if (null == checkCode) {
            return false;
        }
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        return idCard18Code.equalsIgnoreCase(checkCode);
    }

    /**
     * 校验15位身份证
     *
     * <pre>
     * 只校验省份和出生年月日
     * </pre>
     *
     * @param idCard 身份证号码
     * @return boolean {@code true} 是
     */
    public static boolean is15Place(String idCard) {
        if (EmptyUtils.isEmpty(idCard)) {
            return false;
        }
        if (idCard.length() != 15) {// 非15位为假
            return false;
        }
        if (!isDigital(idCard)) {// 15全部为数字
            return false;
        }
        String provinceId = idCard.substring(0, 2);
        if (!checkProvinceId(provinceId)) {// 校验省份
            return false;
        }
        String birthday = idCard.substring(6, 12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        try {
            Date birthDate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthDate);
            if (!tmpDate.equals(birthday)) {// 身份证日期错误
                return false;
            }
        } catch (ParseException e1) {
            return false;
        }
        return true;
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param idCard 身份证号码
     * @return String
     */
    public static String convertIdCarBy15bit(String idCard) {
        if (idCard == null) {
            return null;
        }
        if (idCard.length() != 15) {// 非15位身份证
            return null;
        }
        if (!isDigital(idCard)) {// 15全部为数字
            return null;
        }
        String provinceId = idCard.substring(0, 2);
        // 校验省份
        if (!checkProvinceId(provinceId)) {
            return null;
        }
        String birthday = idCard.substring(6, 12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date birthdate;
        try {
            birthdate = sdf.parse(birthday);
            String tmpDate = sdf.format(birthdate);
            if (!tmpDate.equals(birthday)) {// 身份证日期错误
                return null;
            }
        } catch (ParseException e1) {
            return null;
        }
        Calendar cday = Calendar.getInstance();
        cday.setTime(birthdate);
        String year = String.valueOf(cday.get(Calendar.YEAR));
        String idCard17 = idCard.substring(0, 6) + year + idCard.substring(8);
        char[] c = idCard17.toCharArray();
        // 将字符数组转为整型数组
        int[] bit = converCharToInt(c);
        int sum17 = getPowerSum(bit);
        // 获取和值与11取模得到余数进行校验码
        String checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {// 获取不到校验位
            return null;
        }
        // 将前17位与第18位校验码拼接
        idCard17 += checkCode;
        return idCard17;
    }

    /**
     * 校验省份
     *
     * @param provinceId 省份ID
     * @return boolean {@code true} 正确
     */
    private static boolean checkProvinceId(String provinceId) {
        for (String id : cityCode) {
            if (id.equals(provinceId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数字验证
     *
     * @param str 字符串
     * @return boolean
     */
    private static boolean isDigital(String str) {
        return str.matches("^[0-9]*$");
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit 整型数组
     * @return int
     */
    private static int getPowerSum(int[] bit) {
        int sum = 0;
        if (power.length != bit.length) {
            return sum;
        }
        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < power.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * power[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17 17位和值
     * @return String 校验位
     */
    private static String getCheckCodeBySum(int sum17) {
        String checkCode = null;
        switch (sum17 % 11) {
            case 10:
                checkCode = "2";
                break;
            case 9:
                checkCode = "3";
                break;
            case 8:
                checkCode = "4";
                break;
            case 7:
                checkCode = "5";
                break;
            case 6:
                checkCode = "6";
                break;
            case 5:
                checkCode = "7";
                break;
            case 4:
                checkCode = "8";
                break;
            case 3:
                checkCode = "9";
                break;
            case 2:
                checkCode = "x";
                break;
            case 1:
                checkCode = "0";
                break;
            case 0:
                checkCode = "1";
                break;
        }
        return checkCode;
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param chars 字符串数组
     * @return int[] 整形数组
     * @throws NumberFormatException 数值转换错误
     */
    private static int[] converCharToInt(char[] chars) throws NumberFormatException {
        int[] intArr = new int[chars.length];
        int k = 0;
        for (char temp : chars) {
            intArr[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return intArr;
    }

    /**
     * 验证10位身份编码是否合法
     *
     * @param idCard 身份编码
     * @return 身份证信息数组
     * <p>
     * [0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false)
     * 若不是身份证件号码则返回null
     * </p>
     */
    public static String[] validateIdCard10(String idCard) {
        String[] info = new String[3];
        String card = idCard.replaceAll("[(|)]", "");
        if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
            return null;
        }
        if (idCard.matches("^[a-zA-Z][0-9]{9}$")) { // 台湾
            info[0] = "台湾";
            String char2 = idCard.substring(1, 2);
            if (char2.equals("1")) {
                info[1] = "M";
                System.out.println("MMMMMMM");
            } else if (char2.equals("2")) {
                info[1] = "F";
                System.out.println("FFFFFFF");
            } else {
                info[1] = "N";
                info[2] = "false";
                System.out.println("NNNN");
                return info;
            }
            // info[2] = validateTWCard(idCard) ? "true" : "false";
        } else if (idCard.matches("^[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?$")) { // 澳门
            info[0] = "澳门";
            info[1] = "N";
        } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
            info[0] = "香港";
            info[1] = "N";
            info[2] = validateHKCard(idCard) ? "true" : "false";
        } else {
            return null;
        }
        return info;
    }

    /**
     * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
     * <p>
     * 身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35
     * 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
     * </p>
     * <p>
     * 将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
     * </p>
     *
     * @param idCard 身份证号码
     * @return 验证码是否符合
     */
    public static boolean validateHKCard(String idCard) {
        String card = idCard.replaceAll("[(|)]", "");
        int sum = 0;
        char[] chars1 = card.substring(0, 1).toUpperCase().toCharArray();
        if (card.length() == 9) {
            sum = ((int) chars1[0] - 55) * 9
                    + ((int) card.substring(1, 2).toUpperCase().toCharArray()[0] - 55) * 8;
            card = card.substring(1, 9);
        } else {
            sum = 522 + ((int) chars1[0] - 55) * 8;
        }
        String mid = card.substring(1, 7);
        String end = card.substring(7, 8);
        char[] chars = mid.toCharArray();
        int iflag = 7;
        for (char c : chars) {
            sum = sum + Integer.parseInt(c + "") * iflag;
            iflag--;
        }
        if (end.toUpperCase().equals("A")) {
            sum = sum + 10;
        } else {
            sum = sum + Integer.parseInt(end);
        }
        return sum % 11 == 0;
    }
}
