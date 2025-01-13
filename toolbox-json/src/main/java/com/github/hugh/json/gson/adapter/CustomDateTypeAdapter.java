package com.github.hugh.json.gson.adapter;

import com.github.hugh.constant.DateCode;
import com.github.hugh.util.DateUtils;
import com.github.hugh.util.regex.RegexUtils;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * 自定义兼容多格式日期的解析器
 *
 * @since 2.3.14
 */
public class CustomDateTypeAdapter extends TypeAdapter<Date> {

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getTime());
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            // 如果是数字类型，则将其解析为时间戳（毫秒数），并创建 Date 对象
            case NUMBER:
                long timestamp = in.nextLong();
                String s1 = String.valueOf(timestamp);
                // 判断时间戳长度来区分是秒级还是毫秒级
                if (s1.length() <= 10) {  // 如果时间戳的长度小于等于10位，表示秒级时间戳
                    timestamp *= 1000;  // 转换为毫秒级时间戳
                }
                return new Date(timestamp);
            // 如果是字符串类型，则尝试将其解析为日期
            case STRING:
                String nextString = in.nextString();
                // 如果字符串是纯数字，则将其解析为时间戳
                if (RegexUtils.isNumeric(nextString)) {
                    long timestamp2 = Long.parseLong(nextString);
                    // 判断时间戳长度来区分是秒级还是毫秒级
                    if (nextString.length() <= 10) {  // 如果时间戳的长度小于等于10位，表示秒级时间戳
                        timestamp2 *= 1000;  // 转换为毫秒级时间戳
                    }
                    return new Date(timestamp2);
                    // 否则，尝试将其解析为不同格式的日期字符串
                } else if (DateUtils.verifyDateStr(nextString, DateCode.YEAR_MONTH_DAY_HOUR_MIN_SEC)) {
                    // 尝试解析为 yyyy-MM-dd HH:mm:ss 格式
                    return DateUtils.parse(nextString);
                } else if (DateUtils.verifyDateStr(nextString, DateCode.YEAR_MONTH_DAY)) {
                    // 尝试解析为 yyyy-MM-dd 格式
                    return DateUtils.parse(nextString, DateCode.YEAR_MONTH_DAY);
                } else if (DateUtils.verifyDateStr(nextString, DateCode.CST_FORM)) {
                    // 尝试解析为 CST 格式 (例如：Mon Jan 01 08:00:00 CST 2024)
                    return DateUtils.parse(nextString, DateCode.CST_FORM);
                }
                // 如果以上格式都不匹配，则返回 null
                return null;
            // 如果是 NULL 类型，则读取并消耗掉 NULL 值，并返回 null
            case NULL:
                in.nextNull();
                return null;
            // 如果是其他类型的 JSON 标记，则抛出非法状态异常
            default:
                throw new IllegalStateException("JsonToken：" + peek);
        }
    }
}
