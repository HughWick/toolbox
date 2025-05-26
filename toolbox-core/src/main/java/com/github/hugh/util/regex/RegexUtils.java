package com.github.hugh.util.regex;

import com.github.hugh.constant.StrPool;
import com.github.hugh.util.EmptyUtils;

import java.util.Base64;
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
     * <a href="http://www.unicode.org/reports/tr18/">...</a>
     * <a href="http://www.unicode.org/Public/UNIDATA/UnicodeData.txt">...</a>
     */
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("\\p{P}");

    /**
     * 数字-正则模式
     * <p>
     * 阿拉伯数字、罗马数字等
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\p{N}");

    /**
     * 其他字符-正则模式
     */
//    private static final Pattern OTHER_CHARS_PATTERN = Pattern.compile("\\p{C}");
    private static final Pattern OTHER_CHARS_PATTERN = Pattern.compile("[\\x00-\\x1F\\x7F\\n\\r\\t]");
    /**
     * 邮箱正则表达式
     * <p>
     * <a href="https://blog.csdn.net/Architect_CSDN/article/details/89478042">...</a>
     * <p>
     * 只有英文的邮箱。
     */
    private static final Pattern EMAIL_ENGLISH_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    /**
     * 电话号码正则表达式
     * <ul>
     * <li>中国电信号段 133、149、153、162、173、174、177、180、181、189、190、191、193、199</li>
     * <li>中国联通号段 130、131、132、145、146、155、156、166、175、176、185、186</li>
     * <li>中国移动号段
     * 134(0-8)、135、136、137、138、139、147、148、150、151、152、157、158、159、178、182、183、184、187、188、197、198、195</li>
     * <li>其他号段 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。</li>
     * <li>虚拟运营商 电信：1700、1701、1702</li>
     * <li>移动：1703、1705、1706</li>
     * <li>联通：1704、1707、1708、1709、171</li>
     * <li>卫星通信：1349</li>
     * <li>广电号段：179、192</li>
     * </ul>
     */
    public static final String PHONE_PATTERN_STR = "^((13\\d)|(14[5-9])|(15([0-3]|[5-9]))|(16[2,5-7])|(17[0-9])|(18[0-9])|(19[0-3,5789]))\\d{8}$";
    public static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_PATTERN_STR);

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
     */
    @Deprecated
    private static final Pattern URL_PATTERN = Pattern.compile("^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\??(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&?)*)$");

    /**
     * 网址正则
     */
    private static final Pattern WEB_SITE_PATTERN = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");

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
     * 用于匹配 Base64 编码字符串的正则表达式。
     * <p>
     * 该正则表达式用于验证字符串是否符合 Base64 编码规范，但不进行实际的解码操作。
     * 它检查字符串是否由 4 个 Base64 字符（A-Z, a-z, 0-9, +, /）组成的小组组成，
     * 并且允许末尾有 0、1 或 2 个填充字符“=”。
     * <p>
     * 匹配规则：
     * <ul>
     *     <li>([A-Za-z0-9+/]{4})*：匹配 0 个或多个由 4 个 Base64 字符组成的组。</li>
     *     <li>([A-Za-z0-9+/]{4}：匹配完整的 4 个 Base64 字符的组。</li>
     *     <li>[A-Za-z0-9+/]{3}=：匹配 3 个 Base64 字符后跟一个“=”填充字符的组。</li>
     *     <li>[A-Za-z0-9+/]{2}==：匹配 2 个 Base64 字符后跟两个“=”填充字符的组。</li>
     * </ul>
     * <p>
     * 此正则表达式不处理 data URI 头部。
     */
    public static final Pattern BASE64_PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");
    /**
     * 用于匹配包含 data URI 头部和 Base64 编码数据的字符串的正则表达式。
     * <p>
     * 该正则表达式用于提取 data URI 中的 MIME 类型和 Base64 编码的数据部分。
     * <p>
     * 匹配规则：
     * <ul>
     *     <li>^data:：匹配字符串的开头是否为 "data:"。</li>
     *     <li>([a-z]+/[-a-z]+)?：匹配可选的 MIME 类型，例如 "image/jpeg" 或 "text/plain"。
     *         <ul>
     *             <li>[a-z]+：匹配一个或多个小写字母。</li>
     *             <li>/：匹配斜杠。</li>
     *             <li>[-a-z]+：匹配一个或多个小写字母或连字符。</li>
     *             <li>?：表示 MIME 类型部分是可选的。</li>
     *         </ul>
     *     </li>
     *     <li>;base64,：匹配 ";base64," 分隔符。</li>
     *     <li>(.*)：匹配 Base64 编码的数据部分（捕获组）。</li>
     *     <li>$：匹配字符串的结尾。</li>
     * </ul>
     * <p>
     * 例如：匹配 "data:image/png;base64,iVBORw0KGgo..."，并提取 "image/png" 和 "iVBORw0KGgo..."。
     */
    public static final Pattern BASE64_DATA_URI_REGEX = Pattern.compile("^data:([a-z]+/[-a-z]+)?;base64,(.*)$");
    /**
     * 域名正则表达式
     * <p>
     *
     * @see <a href="https://mathiasbynens.be/demo/url-regex">In search of the perfect URL validation regex</a>
     * </p>
     */
    private static final String DOMAIN_PATTERN = "^(?:" +
            "(?:(?:https?|ftp)://)?" +  // 协议部分（可选）
            "(?:\\S+(?::\\S*)?@)?" +   // 用户名:密码（可选）
            "(?:" +
            // IP 地址排除
            // 私有和本地网络
            "(?!(?:10|127)(?:\\.\\d{1,3}){3})" +
            "(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})" +
            "(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})" +
            // IP 地址点分表示法八位数
            "(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])" +
            "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}" +
            "(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))" +
            "|" +
            // 主机和域名
            "(?:" +
            "(?:[a-z0-9\\u00a1-\\uFFFF][a-z0-9\\u00a1-\\uFFFF_-]{0,62})?[a-z0-9\\u00a1-\\uFFFF]" +
            "\\.)+" +
            // 顶级域名，可能以点结束
            "(?:[a-z\\u00a1-\\uffff]{2,}\\.?)" +
            ")" +
            // 端口号（可选）
            "(?::\\d{2,5})?" +
            // 资源路径（可选）
            "(?:[/?#]\\S*)?$)";

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
     * 中文符号：参考：<a href="https://blog.csdn.net/ztf312/article/details/54310542">...</a>
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
    public static boolean isOtherChars(String string) {
        return isPatternMatch(string, OTHER_CHARS_PATTERN);
    }

    /**
     * 是否包含数字
     * <p>
     * 该方法只能判断字符串中是否包含数字，如果需要判断字符串是否全为数字则调用{@link #isNumeric(String)}
     * </p>
     *
     * @param string 字符
     * @return boolean 包含数字返回{@code true}
     */
    @Deprecated
    public static boolean isNumber(String string) {
        return isPatternMatch(string, NUMBER_PATTERN);
    }

    /**
     * 字符串全都是数字
     * <ul>
     * <li>
     *     2453.11 {@code ->} false
     * </li>
     * <li>
     *     2453a {@code ->} false
     * </li>
     * <li>
     *     2453 {@code ->} true
     * </li>
     * </ul>
     *
     * @param str 字符串
     * @return boolean 字符串全是数字返回{@code true}
     * @since 2.1.9
     */
    public static boolean isNumeric(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 字符串不全都是数字
     *
     * @param str 字符串
     * @return boolean 字符串不为纯数字返回{@code true}
     * @since 2.3.5
     */
    public static boolean isNotNumeric(String str) {
        return !isNumeric(str);
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
     * <p>2.7.14 之后使用{@link #isDomain(String)}</p>
     *
     * @param string 字符
     * @return boolean 结果
     */
    @Deprecated
    public static boolean isUrl(final String string) {
        return isPatternMatch(string, URL_PATTERN);
    }

    /**
     * 验证字符串是一个网址
     * <p>1.5.16 重构了正则,字符串必须是由http or https 开头的url</p>
     * <p>2.7.14 之后使用{@link #isDomain(String)}</p>
     *
     * @param string 结果
     * @return boolean 是否
     */
    @Deprecated
    public static boolean isWebSite(final String string) {
        return WEB_SITE_PATTERN.matcher(string).matches();
    }

    /**
     * 验证字符串不是一个网址
     * <p>2.7.14 之后使用{@link #isNotDomain(String)} (String)}</p>
     *
     * @param string 字符串
     * @return boolean {@code true} 字符串不是网址
     * @since 1.5.16
     */
    @Deprecated
    public static boolean isNotWebSite(final String string) {
        return !isWebSite(string);
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
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
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
     *      经度 longitude： -180.0～+180.0（整数部分为0～180，必须输入1到8位小数）
     *      纬度 latitude ： -90.0～+90.0（整数部分为0～90，必须输入1到8位小数）
     * </pre>
     *
     * @param longitudeCommaLatitude 经度 纬度 格式：{@code 112.944468,28.218373}
     * @return boolean {@code true} 正确的经纬度
     * @since 2.4.8
     */
    public static boolean isLonLat(String longitudeCommaLatitude) {
        final String[] split1 = longitudeCommaLatitude.split(StrPool.COMMA);
        return isLongitude(split1[0]) && isLatitude(split1[1]);
    }

    /**
     * 验证字符串是错误的经纬度
     *
     * @param longitudeCommaLatitude 经度 纬度 格式：{@code 112.944468,28.218373}
     * @return boolean {@code true} 错误的经纬度
     * @since 2.4.8
     */
    public static boolean isNotLonLat(String longitudeCommaLatitude) {
        return !isLonLat(longitudeCommaLatitude);
    }

    /**
     * 只校验正数 0-90.000000 0-180.000000 范围内
     * 经纬度校验
     * <pre>
     *      经度 longitude： -180.0～+180.0（整数部分为0～180，必须输入1到8位小数）
     *      纬度 latitude ： -90.0～+90.0（整数部分为0～90，必须输入1到8位小数）
     * </pre>
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return boolean {@code true} 正确的经纬度
     * @since 1.4.16
     */
    public static boolean isLonLat(String longitude, String latitude) {
        return isLongitude(longitude) && isLatitude(latitude);
    }

    /**
     * 验证字符串是错误的经纬度
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
     * @param longitude 经度
     * @return boolean {@code true} 经度正确
     * @since 1.4.16
     */
    public static boolean isLongitude(String longitude) {
        if (EmptyUtils.isEmpty(longitude)) {
            return false;
        }
        String longitudePattern = "^[\\-+]?(0?\\d{1,2}\\.\\d{1,8}|1[0-7]?\\d\\.\\d{1,8}|180\\.0{1,8})$";
        longitude = longitude.strip();
        return Pattern.matches(longitudePattern, longitude);
    }

    /**
     * 字符串不是正确的经度
     *
     * @param longitude 经度
     * @return boolean {@code true}错误的经度
     * @since 2.4.5
     */
    public static boolean isNotLongitude(String longitude) {
        return !isLongitude(longitude);
    }

    /**
     * 只校验正数 0-90.000000 范围内
     * 纬度校验
     * <pre>
     *      纬度 latitude ： -90.0～+90.0（整数部分为0～90，必须输入1到8位小数）
     * </pre>
     *
     * @param latitude 纬度
     * @return boolean {@code true} 纬度正确
     * @since 1.4.16
     */
    public static boolean isLatitude(String latitude) {
        if (EmptyUtils.isEmpty(latitude)) {
            return false;
        }
        String latitudePattern = "^[\\-+]?([0-8]?\\d\\.\\d{1,8}|90\\.0{1,8})$";
        latitude = latitude.strip();
        return Pattern.matches(latitudePattern, latitude);
    }

    /**
     * 验证错误是否为错误的纬度
     *
     * @param latitude 纬度
     * @return boolean {@code true}错误的纬度
     * @since 2.4.5
     */
    public static boolean isNotLatitude(String latitude) {
        return !isLatitude(latitude);
    }

    /**
     * 数为偶数
     *
     * @param number 数
     * @return boolean 是偶数返回{@code true}
     * @since 2.4.3
     */
    public static boolean isEvenNumber(long number) {
        return (number % 2) == 0;
    }

    /**
     * 数不是偶数
     *
     * @param number 数
     * @return boolean 不是偶数返回{@code true}
     * @since 2.4.3
     */
    public static boolean isNotEvenNumber(long number) {
        return !isEvenNumber(number);
    }

    /**
     * 验证字符串是否为小写与数组组合
     *
     * @param string 字符串
     * @return boolean 全都是小写与数字{@code true}
     * @since 2.4.3
     */
    public static boolean isLowerCaseAndNumber(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        return string.matches("^[0-9a-z]+$");
    }

    /**
     * 验证字符串是否为小写与数组组合
     *
     * @param string 字符串
     * @return boolean 不是小写与数字{@code true}
     * @since 2.4.3
     */
    public static boolean isNotLowerCaseAndNumber(String string) {
        return !isLowerCaseAndNumber(string);
    }

    /**
     * 验证字符串是否为大写与数组组合
     *
     * @param string 字符串
     * @return boolean 不是大写与数字组合{@code true}
     * @since 2.4.3
     */
    public static boolean isUpperCaseAndNumber(String string) {
        if (EmptyUtils.isEmpty(string)) {
            return false;
        }
        return string.matches("^[0-9A-Z]+$");
    }

    /**
     * 验证字符串是否为大写与数组组合
     *
     * @param string 字符串
     * @return boolean 不是大写与数字组合{@code true}
     * @since 2.4.3
     */
    public static boolean isNotUpperCaseAndNumber(String string) {
        return !isUpperCaseAndNumber(string);
    }

    /**
     * 检查给定的字符串是否是合法的 Base64 编码字符串。
     *
     * @param str 要验证的字符串。
     * @return 如果字符串是合法的 Base64 编码字符串，则返回 {@code true}，否则返回 {@code false}。
     * @since 2.6.1
     */
    public static boolean isBase64(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 1. 尝试匹配 data URI 头部
        Matcher matcher = BASE64_DATA_URI_REGEX.matcher(str);
        if (matcher.matches()) {
            str = matcher.group(2); // 提取 Base64 数据部分
        }
        // 2. 移除空白字符 (可选，但推荐)
        str = str.replaceAll("\\s", "");
        // 3. 基本长度检查 (优化性能)
        if (str.length() % 4 != 0) {
            return false;
        }
        // 4. 使用正则表达式进行初步校验 (可选，但推荐)
        if (!BASE64_PATTERN.matcher(str).matches()) {
            return false;
        }
        // 5. 最终校验：尝试解码
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException illegalArgumentException) {
            return false; // 解码失败，不是有效的 Base64
        }
    }

    /**
     * 判断给定的字符串是否不是 Base64 编码。
     *
     * @param str 待判断的字符串
     * @return 如果给定的字符串不是 Base64 编码，则返回 true；否则返回 false。
     * @since 2.6.1
     */
    public static boolean isNotBase64(String str) {
        return !isBase64(str);
    }

    /**
     * 验证给定的字符串是否都为十六进制字符。
     *
     * @param str 待验证的字符串
     * @return 如果字符串都为十六进制字符，则返回true；否则返回false
     * @since 2.6.7
     */
    public static boolean isHexadecimal(String str) {
        // 判断字符串是否为空
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 使用正则表达式匹配判断字符串是否都为十六进制字符
        String hexadecimalPattern = "^[0-9a-fA-F]+$";
        return Pattern.matches(hexadecimalPattern, str);
    }

    /**
     * 验证给定的字符串是否不都为十六进制字符。
     *
     * @param str 待验证的字符串
     * @return 如果字符串不都为十六进制字符，则返回true；否则返回false
     * @since 2.6.7
     */
    public static boolean isNotHexadecimal(String str) {
        return !isHexadecimal(str);
    }

    /**
     * 检查给定的字符串是否为有效的域名或 URL。
     *
     * @param str 要检查的字符串
     * @return 如果字符串是有效的域名或 URL，则返回 true；否则返回 false
     * @since 2.7.14
     */
    public static boolean isDomain(String str) {
        if (EmptyUtils.isEmpty(str)) {
            return false; // 如果字符串为空，返回 false
        }
        Pattern pattern = Pattern.compile(DOMAIN_PATTERN); // 编译正则表达式
        Matcher matcher = pattern.matcher(str); // 创建匹配器
        return matcher.matches(); // 返回是否匹配
    }

    /**
     * 检查给定的字符串是否不是有效的域名或 URL。
     *
     * @param str 要检查的字符串
     * @return 如果字符串不是有效的域名或 URL，则返回 true；否则返回 false
     * @since 2.7.14
     */
    public static boolean isNotDomain(String str) {
        return !isDomain(str); // 调用 isDomain 方法并取反结果
    }
}
