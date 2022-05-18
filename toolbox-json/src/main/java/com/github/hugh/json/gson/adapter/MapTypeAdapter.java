package com.github.hugh.json.gson.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * gson map 解析类型适配器
 * <p>gson 默认识别 Number（包括int、long、float、double等）型,都会被强制转化成double,至于为什么这么做,因为这里所有的类型都可以转换成double，而反过来则不行.</p>
 * <p>进行重写,改写数字的处理逻辑，将数字值分为整型与浮点型</p>
 *
 * @author hugh
 * @since 1.4.0
 */
public class MapTypeAdapter extends TypeAdapter<Object> {

    private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

    @Override
    public Object read(JsonReader in) throws IOException {
        JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;
            case BEGIN_OBJECT:
                Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;
            case STRING:
                return in.nextString();
            case NUMBER:// 改写数字的处理逻辑，将数字值分为整型与浮点型。
                double dbNum = in.nextDouble();
                // 数字超过long的最大值，返回浮点类型
                if (dbNum > Long.MAX_VALUE) {
                    return String.valueOf(dbNum);
                }
                // 判断数字是否为整数值
                long lngNum = (long) dbNum;
                if (dbNum == lngNum) {
                    return String.valueOf(lngNum);
                } else {
                    return String.valueOf(dbNum);
                }
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void write(JsonWriter out, Object value) throws IOException {
        delegate.write(out, value);
    }
}
