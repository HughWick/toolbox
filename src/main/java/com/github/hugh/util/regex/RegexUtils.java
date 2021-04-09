package com.github.hugh.util.regex;

import com.github.hugh.util.EmptyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author hugh
 * @since 1.0.6
 */
public class RegexUtils {
    private RegexUtils() {
    }

    /**
     * 特殊字符
     */
    private static final String[] SPECIAL_CHARS = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};

    /**
     * 标点符号正则
     * <p>
     * P 其中的小写 p 是 property 的意思，表示 Unicode 属性，用于 Unicode 正表达式的前缀。
     * <p>
     * 等价于：
     *
     * <pre>
     * Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]");
     * </pre>
     * 大写 P 表示 Unicode 字符集七个字符属性之一：标点字符。
     * 其他六个是
     * L：字母；
     * M：标记符号（一般不会单独出现）；
     * Z：分隔符（比如空格、换行等）；
     * S：符号（比如数学符号、货币符号等）；
     * N：数字（比如阿拉伯数字、罗马数字等）；
     * C：其他字符
     * <p>
     * 相关信息：
     * http://www.unicode.org/reports/tr18/
     * http://www.unicode.org/Public/UNIDATA/UnicodeData.txt
     */
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\p{P}");

    /**
     * 字母-正则模式
     */
    private static final Pattern LETTER_PATTERN = Pattern.compile("\\p{L}");

    /**
     * 标记性-正则模式
     */
    private static final Pattern MARKABLE_PATTERN = Pattern.compile("\\p{M}");

    /**
     * 分隔符-正则模式
     * <p>
     * 空格、换行等
     */
    private static final Pattern DELIMITER_PATTERN = Pattern.compile("\\p{Z}");

    /**
     * 符号-正则模式
     * <p>
     * 数学符号、货币符号
     */
    private static final Pattern SYMBOL_PATTERN = Pattern.compile("\\p{S}");

    /**
     * 数字-正则模式
     * <p>
     * 阿拉伯数字、罗马数字等
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\p{N}");

    /**
     * 其他字符-正则模式
     */
    private static final Pattern OTHER_CHARS_PATTERN = Pattern.compile("\\p{C}");

    /**
     * 邮箱正则表达式
     * <p>
     * https://blog.csdn.net/Architect_CSDN/article/details/89478042
     * <p>
     * 只有英文的邮箱。
     */
    private static final Pattern EMAIL_ENGLISH_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 允许中文前缀的邮箱正则表达式
     * <p>
     * https://www.cnblogs.com/lst619247/p/9289719.html
     */
    private static final Pattern EMAIL_CHINESE_PATTERN = Pattern.compile("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 电话号码正则表达式
     * <ul>
     * <li>中国电信号段 133、149、153、173、177、179、180、181、189、191、199</li>
     * <li>中国联通号段 130、131、132、145、155、156、166、175、176、185、186</li>
     * <li>中国移动号段
     * 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198、195</li>
     * <li>其他号段 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。</li>
     * <li>虚拟运营商 电信：1700、1701、1702</li>
     * <li>移动：1703、1705、1706</li>
     * <li>联通：1704、1707、1708、1709、171</li>
     * <li>卫星通信：1349</li>
     * </ul>
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(16([5-7]))|(17[0,1,2,3,5,6,7,8,9])|(18[0-9])|(19[1|5|8|9]))\\d{8}$");

    /**
     * URL 正则表达式
     * <p>
     * （1）验证http,https,ftp开头
     * （2）验证一个":"，验证多个"/"
     * （3）验证网址为 xxx.xxx
     * （4）验证有0个或1个问号
     * （5）验证参数必须为xxx=xxx格式，且xxx=空格式通过
     * （6）验证参数与符号&连续个数为0个或1个
     * <p>
     * https://www.cnblogs.com/woaiadu/p/7084250.html
     */
    private static final Pattern URL_PATTERN = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$");

    /**
     * 网址正则
     */
    private static final Pattern WEB_SITE_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
//    private static final Pattern WEB_SITE_PATTERN = Pattern.compile("^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$");

    /**
     * sql 关键字-正则表达式
     * <ul>
     * <li>\\b  表示 限定单词边界  比如  select 不通过   1select则是可以的</li>
     * </ul>
     **/
    private static final String SQL_PATTERN = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|drop|execute)\\b)";

    /**
     * IP正则表达式
     */
    private static final Pattern IP_PATTERN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    /**
     * 端口正则表达式
     */
    private static final Pattern PORT_PATTERN = Pattern.compile("([0-9]|[1-9]\\d{1,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])");

    /**
     * 全中文-正则
     */
    private static final Pattern FULL_CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+");

    /**
     * 特殊字符-正则
     */
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[ _`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]|\n|\r|\t");

    /**
     * 对特殊字符转译
     *
     * @param keyword 特殊字符
     * @return String 结果
     */
    public static String escapeWord(String keyword) {
        if (EmptyUtils.isNotEmpty(keyword)) {
            for (String key : SPECIAL_CHARS) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    /**
     * 是否为标点符号
     * 中文符号：参考：https://blog.csdn.net/ztf312/article/details/54310542
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isPunctuation(String string) {
        return isPatternMatch(string, PUNCTUATION_PATTERN);
    }

    /**
     * 是否为可标记的符号
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isMarkable(String string) {
        return isPatternMatch(string, MARKABLE_PATTERN);
    }

    /**
     * 是否为字符
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isSymbol(String string) {
        return isPatternMatch(string, SYMBOL_PATTERN);
    }

    /**
     * 是否为可标记的符号
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isOtherChars(String string) {
        return isPatternMatch(string, OTHER_CHARS_PATTERN);
    }

    /**
     * 是否为数字
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isNumber(String string) {
        return isPatternMatch(string, NUMBER_PATTERN);
    }

    /**
     * 验证字符串是邮箱标准格式
     *
     * @param string 字符
     * @return boolean {@code true} 是邮箱格式
     */
    public static boolean isEmail(final String string) {
        return isPatternMatch(string, EMAIL_ENGLISH_PATTERN);
    }

    /**
     * 字符串不是邮箱格式
     *
     * @param string 字符
     * @return boolean {@code true} 不是邮箱格式
     * @since 1.5.10
     */
    public static boolean isNotEmail(final String string) {
        return !isEmail(string);
    }

    /**
     * 是否为URL
     *
     * @param string 字符
     * @return boolean 结果
     */
    public static boolean isUrl(final String string) {
        return isPatternMatch(string, URL_PATTERN);
    }

    /**
     * 是否为网址
     *
     * @param string 结果
     * @return boolean 是否
     */
    public static boolean isWebSite(final String string) {
        return WEB_SITE_PATTERN.matcher(string).matches();
//        return isPatternMatch(string, WEB_SITE_PATTERN);
    }

    /**
     * 验证字符串是否为手机号码
     *
     * @param mobile 手机号码
     * @return boolean {@code true} 正确
     */
    public static boolean isPhone(String mobile) {
        if (EmptyUtils.isEmpty(mobile) || mobile.length() != 11) {
            return false;
        } else {
            Matcher m = PHONE_PATTERN.matcher(mobile);
            return m.matches();
        }
    }

    /**
     * 验证字符串不是手机号码
     *
     * @param mobile 手机号码
     * @return boolean {@code true} 不是
     * @since 1.2.8
     */
    public static boolean isNotPhone(String mobile) {
        return !isPhone(mobile);
    }

    /**
     * 是否包含sql语句字符串
     *
     * @param str 字符串
     * @return boolean {@code true} 存在
     */
    public static boolean isSql(String str) {
        return isPatternMatch(str, Pattern.compile(SQL_PATTERN, Pattern.CASE_INSENSITIVE));
    }

    /**
     * 校验ip地址格式是否正确
     *
     * @param string 字符串
     * @return boolean {@code true} 正确
     */
    public static boolean isIp(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        return IP_PATTERN.matcher(string).matches();
    }

    /**
     * 不是IP地址
     *
     * @param string 字符串
     * @return boolean {@code true} 不是IP
     */
    public static boolean isNotIp(String string) {
        return !isIp(string);
    }


    /**
     * 校验字符串是否为正确的端口
     *
     * @param string 字符串
     * @return boolean {@code true} 正确
     */
    public static boolean isPort(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        return PORT_PATTERN.matcher(string).matches();
    }

    /**
     * 校验字符串为不正确的端口
     *
     * @param string 字符串
     * @return boolean {@code true} 错误端口
     * @since 1.1.2
     */
    public static boolean isNotPort(String string) {
        return !isPort(string);
    }

    /**
     * 验证字符串是否匹配正则表达式
     *
     * @param string  字符串
     * @param pattern 正则表达式
     * @return boolean 是否匹配
     */
    protected static boolean isPatternMatch(final String string, final Pattern pattern) {
        return pattern.matcher(string).find();
    }

    /**
     * 判断字符串全是中文
     *
     * @param str 待校验字符串
     * @return boolean {@code true}全中文,其他返回{@code false}
     * @since 1.4.3
     */
    public static boolean isFullChinese(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        return FULL_CHINESE_PATTERN.matcher(str).matches();
    }

    /**
     * 校验字符串,如果不全是中文返回{@code true}
     *
     * @param str 待校验字符串
     * @return boolean {@code true}不全是中文
     * @since 1.4.3
     */
    public static boolean isNotFullChinese(String str) {
        return !isFullChinese(str);
    }

    /**
     * 只校验正数 0-90.000000 0-180.000000 范围内
     * 经纬度校验
     * <pre>
     *      经度longitude： -180.0～+180.0（整数部分为0～180，必须输入1到8位小数）
     *      纬度 latitude ： -90.0～+90.0（整数部分为0～90，必须输入1到8位小数）
     * </pre>
     *
     * @return boolean {@code true} 正确的经纬度
     * @since 1.4.16
     */
    public static boolean isLonLat(String longitude, String latitude) {
        return isLongitude(longitude) && isLatitude(latitude);
    }

    /**
     * 经纬度错误
     *
     * @param longitude 经度
     * @param latitude  维度
     * @return boolean  {@code true} 错误的经纬度
     * @since 1.4.16
     */
    public static boolean isNotLonLat(String longitude, String latitude) {
        return !isLonLat(longitude, latitude);
    }

    /**
     * 只校验正数 0-180.000000 范围内
     * 经度校验
     * <pre>
     *      经度longitude： -180.0～+180.0（整数部分为0～180，必须输入1到8位小数）
     * </pre>
     *
     * @return boolean {@code true} 经度正确
     * @since 1.4.16
     */
    public static boolean isLongitude(String longitude) {
        if (EmptyUtils.isEmpty(longitude)) {
            return false;
        }
        String longitudePattern = "^[\\-+]?(0?\\d{1,2}\\.\\d{1,8}|1[0-7]?\\d{1}\\.\\d{1,8}|180\\.0{1,8})$";
        longitude = longitude.trim();
        return Pattern.matches(longitudePattern, longitude);
    }

    /**
     * 只校验正数 0-90.000000 范围内
     * 纬度校验
     * <pre>
     *      纬度 latitude ： -90.0～+90.0（整数部分为0～90，必须输入1到8位小数）
     * </pre>
     *
     * @return boolean {@code true} 维度正确
     * @since 1.4.16
     */
    public static boolean isLatitude(String latitude) {
        if (EmptyUtils.isEmpty(latitude)) {
            return false;
        }
        String latitudePattern = "^[\\-+]?([0-8]?\\d{1}\\.\\d{1,8}|90\\.0{1,8})$";
        latitude = latitude.trim();
        return Pattern.matches(latitudePattern, latitude);
    }
}
