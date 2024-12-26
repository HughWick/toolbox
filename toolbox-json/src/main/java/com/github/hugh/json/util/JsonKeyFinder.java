package com.github.hugh.json.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * `JsonKeyFinder` 类提供了一些静态方法，用于处理 JSON 字符串，例如提取键等。
 *
 * @since 2.7.19
 */
public class JsonKeyFinder {
    private JsonKeyFinder() {
    }

    /**
     * 从 JSON 字符串中提取所有键（key），并可选择忽略指定的键。
     *
     * @param jsonString 要解析的 JSON 字符串。
     * @param ignoreKeys 可变参数，表示需要忽略的键。可以传入多个需要忽略的键，也可以不传，表示不忽略任何键。
     * @return 返回一个包含所有提取的键的列表（{@code List<String>}），列表中不包含被忽略的键。
     */
    public static List<String> getAllKeys(String jsonString, String... ignoreKeys) {
        List<String> keys = new ArrayList<>();
        // 使用正则表达式匹配键
        Pattern pattern = Pattern.compile("\"([^\"]+)\":");
        Matcher matcher = pattern.matcher(jsonString);
        // 将 ignoreKeys 转换为 List，方便使用 contains 方法判断是否忽略。
        List<String> ignoreList = ignoreKeys == null ? null : Arrays.asList(ignoreKeys);
        while (matcher.find()) {
            // 提取键，并去除前后空格。
            String key = matcher.group(1).trim().strip();
            // 如果 ignoreList 为 null 或者 不包含当前key，则将该键添加到结果列表中。
            if (ignoreList == null || !ignoreList.contains(key)) {
                keys.add(key);
            }
        }
        return keys;
    }
}
