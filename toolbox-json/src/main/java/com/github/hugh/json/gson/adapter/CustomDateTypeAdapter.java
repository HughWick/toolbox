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
            case NUMBER:
                return new Date(in.nextLong());
            case STRING:
                String s = in.nextString();
                if (RegexUtils.isNumeric(s)) {
                    return new Date(Long.parseLong(s));
                } else if (DateUtils.isDateFormat(s)) {
                    return DateUtils.parse(s);
                } else if (DateUtils.isDateFormat(s, DateCode.YEAR_MONTH_DAY)) {
                    return DateUtils.parse(s, DateCode.YEAR_MONTH_DAY);
                }
                return null;
            case NULL:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException("JsonToken：" + peek);
        }
//        if (in != null) {
//            System.out.println("---1-read>" + in.nextLong());
//            return new Date(in.nextLong());
//        } else {
//            return null;
//        }
    }
}
